import React, { useEffect, useState } from "react";
import axios from "axios";
import feedbackRatingClient from "../AxiosClient/FeedbackRatingClient";
import { useSelector } from "react-redux";
import { FaBeer, FaUserAlt } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import { FaPencilAlt } from "react-icons/fa";
import { FaStar, FaRegStar, FaUserPlus } from "react-icons/fa";

const ProjectAllocation = () => {
  const [projects, setProjects] = useState([]);
  const isViewOnly = useSelector((state) => state.popup.isReadOnly);
  const isEditOnly = useSelector((state) => state.editProfile.isEdit);
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isFormVisible, setIsFormVisible] = useState(false); // State to toggle form visibility
  const [clientName, setClientName] = useState("");
  const [selectedProject, setSelectedProject] = useState(null);
  const [isEditing, setIsEditing] = useState(true);
  const [isChecked, setIsChecked] = useState(false);
  const [assignProject, setAssignProject] = useState([]);

  const [formData, setFormData] = useState({
    feedbacks: [],
  });

  const [feedbacks, setFeedbacks] = useState([]);
  const [newData, setNewData] = useState({
    projectName: "",
    startDate: "",
    endDate: "",
    ratings: 0,
    review: "",
    givenBy: "",
  });

  useEffect(() => {
    axios
      .get("http://localhost:8040/projects")
      .then((response) => {
        setProjects(response.data);
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
      });
  }, []);

  const handleAssignProject = async (event) => {
    setClientName(""); // Reset client name field
    setSelectedProject(null); // Reset selected project
    setIsChecked(false); // Reset checkbox value
    setIsEditing(false);
    setIsEditing(true);
    const payload = {
      profileId: currentProfileId, // Getting from Redux state
      projectId: selectedProject.projectId,
      clientName: clientName,
      projectName: selectedProject.projectName,
      assigned: true,
      dedicated: isChecked
    };

    try {
      const response = await axios.post(
        "http://localhost:8045/api/assignments",
        payload
      );

      if (response.status === 201 || response.status === 200) {
        toast.success(
          `Project ${selectedProject.projectName} assigned successfully!`
        );

        fetchAssignedProjects();
      } else {
        toast.error("Failed to assign project. Please try again.");
      }
    } catch (error) {
      console.error("Error assigning project:", error);
      toast.error("Error assigning project. Please check console for details.");
    }
  };

  const handleFeedbackChange = (field, value) => {
    setNewData((prevState) => ({
      ...prevState,
      [field]: value,
    }));
  };

  const handleAddFeedback = () => {
    setNewData({
      projectName: "",
      startDate: "",
      endDate: "",
      ratings: 0,
      review: "",
      givenBy: "",
    });
    setIsFormOpen(true);
    setIsFormVisible(true);
  };

  const fetchFeedback = async () => {
    try {
      const res = await feedbackRatingClient.get(`/${currentProfileId}`);
      if (res && res.data && Array.isArray(res.data.feedbacks)) {
        const fetchedFeedbacks = res.data.feedbacks;
        setFeedbacks(fetchedFeedbacks);
        setFormData((prevFormData) => ({
          ...prevFormData,
          feedbacks: fetchedFeedbacks,
        }));
      } else {
        console.error("No feedbacks found in the response");
        setFeedbacks([]);
      }
    } catch (error) {
      console.error("Error fetching feedbacks: ", error);
      setFeedbacks([]);
    }
  };

  const fetchAssignedProjects = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8045/api/assignments/projects/${currentProfileId}`
      );

      if (response.status === 200) {
        setAssignProject(response.data);
        console.log(response.data + "sdwqdwqd");
      }
    } catch (error) {
      console.error("Error fetching assigned projects:", error);
    }
  };

  useEffect(() => {
    fetchAssignedProjects();
  }, []);

  useEffect(() => {
    fetchFeedback();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const updatedFeedbacks = [...feedbacks, newData];

    const feedbackData = {
      feedbacks: updatedFeedbacks,
    };

    try {
      const feedbackRes = await feedbackRatingClient.put(
        `/${currentProfileId}`,
        feedbackData
      );

      if (feedbackRes.status === 200) {
        toast.success("Feedback added successfully!", {
          position: "top-right",
          autoClose: 3000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        });

        // Update local state with new feedback
        setFeedbacks(updatedFeedbacks);
        setFormData((prev) => ({
          ...prev,
          feedbacks: updatedFeedbacks,
        }));

        // Reset form
        setIsFormOpen(false);
        setIsFormVisible(false);
        setNewData({
          projectName: "",
          startDate: "",
          endDate: "",
          ratings: 0,
          review: "",
          givenBy: "",
        });
      }
    } catch (error) {
      console.error("Error submitting feedback:", error);
      toast.error("Failed to submit feedback. Please try again.");
    }
  };

  const [hoverRating, setHoverRating] = useState(null);

  const handleMouseEnter = (rating) => {
    setHoverRating(rating); // Set the hover rating
  };

  const handleMouseLeave = () => {
    setHoverRating(null); // Reset hover rating when mouse leaves
  };

  const handleClickRating = (rating) => {
    handleFeedbackChange("ratings", rating); // Update rating based on the clicked star
  };

  const handleClose = () => {
    // Your logic to handle closing the form or navigating away
    console.log("Form closed");
    setIsFormOpen(false);
    setIsFormVisible(false);
  };

  const handleCancel = () => {
    setIsEditing(true);
  };

  const handleAssign = () => {
    setIsEditing(false);
  };

  const handleCheckboxChange = () => {
    setIsChecked(!isChecked);
  };
  console.log(isChecked);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const options = { day: "2-digit", month: "long", year: "numeric" };
    return date.toLocaleDateString("en-GB", options); // 'en-GB' to get day month year format
  };

  return (
    <form className="Project-Allocation-form" onSubmit={handleSubmit}>
      <div className="mx-auto rounded-lg">
        <div className="bg-white mx-auto">
          <div className="flex items-center justify-between mb-4 mt-[-20px]">
            {/* Project Heading */}
            <h2 className="text-xl font-semibold text-gray-800 font-roboto">
              Projects
            </h2>
            {isEditing ? (
              <button
                className="px-4 py-1.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition shadow-md"
                onClick={handleAssign}
              >
                <FaUserPlus className="inline text-xl" /> &nbsp; Assign{" "}
              </button>
            ) : (
              <div className="flex gap-3">
                <button
                  className="px-4 py-2 bg-white text-blue rounded-lg shadow-md"
                  onClick={handleCancel}
                >
                  Cancel
                </button>

                <button
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition shadow-md"
                  onClick={handleAssignProject}
                >
                  Save
                </button>
              </div>
            )}
          </div>

          {!isEditing && (
            <div className="w-full p-5 rounded-[10px] bg-gray-100 mb-5">
              <div className="flex justify-between gap-10">
                <div className="mb-4 w-[50%]">
                  <label className="block text-gray-600 text-sm mb-1 flex items-center gap-2 font-bold">
                    Client Name
                  </label>
                  <input
                    type="text"
                    placeholder="Select client"
                    className="w-full p-3 border focus:outline-blue-500 bg-white"
                    value={clientName}
                    onChange={(e) => setClientName(e.target.value)}
                  />
                </div>

                {/* Project Selection Dropdown */}
                <div className="mb-4 w-[50%]">
                  <label className="block text-gray-600 text-sm mb-1 font-bold">
                    Project Name
                  </label>
                  <select
                    className="w-full p-3 border focus:outline-blue-500 bg-white"
                    value={selectedProject?.projectId || ""}
                    onChange={(e) => {
                      const project = projects.find(
                        (p) => p.projectId === e.target.value
                      );
                      setSelectedProject(project);
                    }}
                  >
                    <option value="" disabled>
                      Select project
                    </option>
                    {projects.map((project) => (
                      <option key={project.projectId} value={project.projectId}>
                        {project.projectName} ({project.projectId})
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="w-[48%] flex justify-between items-center">
                <label className="block text-gray-600 text-sm mb-1 flex items-center gap-2 font-bold">
                  Dedicate Resource
                </label>
                <label className="flex cursor-pointer select-none items-center">
                  <div className="relative">
                    <input
                      type="checkbox"
                      checked={isChecked}
                      onChange={handleCheckboxChange}
                      className="sr-only"
                    />
                    <div
                      className={`box block h-6 w-10 rounded-full ${
                        isChecked ? "bg-[#0B6EFD]" : "bg-black"
                      }`}
                    ></div>
                    <div
                      className={`absolute left-1 top-1 flex h-4 w-4 items-center justify-center rounded-full bg-white transition ${
                        isChecked ? "translate-x-full" : ""
                      }`}
                    ></div>
                  </div>
                </label>
              </div>
            </div>
          )}
        </div>

        <div className="mx-auto rounded-lg p-1">
          {assignProject.length === 0
            ? "No Assigned projects"
            : [...assignProject].reverse().map((project, index) => {
                return (
                  <div
                    key={index}
                    className="bg-gray-100 p-4 rounded-lg mb-4 shadow-lg border border-gray-200"
                  >
                    <div className="flex-col items-center justify-between mb-4">
                      <h3 className="text-xl font-semibold text-gray-800 m-2">
                        {project.projectName}
                      </h3>

                      <h3 className="text-small text-gray-600 m-2">
                        {project.clientName}
                      </h3>
                    </div>
                  </div>
                );
              })}
        </div>

        {/* Feedbacks Section */}
        <div className="mx-auto rounded-lg pt-4">
          <div className="flex justify-between items-center">
            <h2 className="text-xl font-semibold text-gray-800 font-roboto mb-2">
              {" "}
              Project Feedback
            </h2>
            {!isFormVisible && (
              <button
                type="button"
                onClick={handleAddFeedback}
                className=" bg-white-500 text-blue font-bold px-3 py-1 rounded-md shadow-sm text-sm border-[1px] border-[solid] border-[black]"
              >
                <FaPencilAlt className="inline" /> &nbsp; Add Feedback{" "}
              </button>
            )}

            {isFormVisible && (
              <div className="flex justify-end gap-4 ">
                {/* Close Button */}
                <button
                  type="button"
                  onClick={handleClose} // Function to close or navigate away
                  className="px-4 py-2 bg-white text-blue rounded-lg shadow-md"
                >
                  Cancel
                </button>

                {/* Submit Button */}
                <button
                  type="submit"
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition shadow-md"
                >
                  Save
                </button>
              </div>
            )}
          </div>

          {isFormOpen && (
            <div className="bg-gray-100 p-6 rounded-lg mb-4 mt-4 shadow-lg border border-gray-200">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-4">
                {/* Project Name */}
                <div className="relative">
                  <label className="text-gray-700 text-sm font-semibold">
                    Project Name
                  </label>
                  <select
                    className="w-full p-3 border focus:outline-blue-500 bg-white"
                    value={selectedProject?.projectId || ""}
                    onChange={(e) => {
                      const project = projects.find(
                        (p) => p.projectId === e.target.value
                      );
                      setSelectedProject(project);

                      // Update newData with the selected project's projectName
                      handleFeedbackChange(
                        "projectName",
                        project?.projectName || ""
                      );
                    }}
                  >
                    <option value="" disabled>
                      Select project
                    </option>
                    {projects.map((project) => (
                      <option key={project.projectId} value={project.projectId}>
                        {project.projectName} ({project.projectId})
                      </option>
                    ))}
                  </select>
                </div>

                {/* Start Date */}
                <div className="relative">
                  <label className="text-gray-700 text-sm font-semibold">
                    Start Date
                  </label>
                  <input
                    type="date"
                    value={newData.startDate}
                    onChange={(e) =>
                      handleFeedbackChange("startDate", e.target.value)
                    }
                    disabled={isViewOnly}
                    className="w-full bg-white border border-gray-300 rounded-md p-3 text-sm focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>

                {/* End Date */}
                <div className="relative">
                  <label className="text-gray-700 text-sm font-semibold">
                    End Date
                  </label>
                  <input
                    type="date"
                    value={newData.endDate}
                    onChange={(e) =>
                      handleFeedbackChange("endDate", e.target.value)
                    }
                    disabled={isViewOnly}
                    className="w-full bg-white border border-gray-300 rounded-md p-3 text-sm focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
              </div>

              {/* Reviewer Name and Rating Labels on Same Line */}
              <div className="relative mb-4">
                {/* Labels (Reviewer Name and Rating) on the same line */}
                <div className="flex items-center space-x-4 mb-2">
                  {/* Reviewer Label */}
                  <label className="text-gray-700 text-sm font-semibold">
                    Reviewer Name
                  </label>

                  {/* Rating Label */}
                </div>

                {/* Inputs (Reviewer Name and Rating) on the next line */}
                <div className="flex items-center space-x-28">
                  {/* Reviewer Name Input */}
                  <div className="w-[50%]">
                    <input
                      type="text"
                      placeholder="Enter Reviewer Name"
                      value={newData.givenBy}
                      onChange={(e) =>
                        handleFeedbackChange("givenBy", e.target.value)
                      }
                      disabled={isViewOnly}
                      className="bg-white border border-gray-300 rounded-md p-3 text-sm focus:ring-2 focus:ring-indigo-500 focus:outline-none w-full"
                    />
                  </div>

                  {/* Rating with Stars */}

                  <div>
                    <label className="text-gray-700 text-sm font-semibold">
                      Rating
                    </label>

                    <div className="flex items-center space-x-1">
                      {[1, 2, 3, 4, 5].map((rating) => (
                        <div
                          key={rating}
                          onMouseEnter={() => handleMouseEnter(rating)} // Hover event
                          onMouseLeave={handleMouseLeave} // Reset hover event
                          onClick={() => handleClickRating(rating)} // Click event
                          className="cursor-pointer"
                        >
                          {newData.ratings >= rating ||
                          hoverRating >= rating ? (
                            <FaStar className="text-yellow-500" size={24} />
                          ) : (
                            <FaRegStar className="text-gray-300 " size={24} />
                          )}
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              </div>

              {/* Feedback Textarea */}
              <div className="relative mb-4">
                <label className="text-gray-700 text-sm font-semibold">
                  Feedback
                </label>
                <textarea
                  placeholder="Write your Feedback here"
                  value={newData.review}
                  onChange={(e) =>
                    handleFeedbackChange("review", e.target.value)
                  }
                  disabled={isViewOnly}
                  className="w-full bg-white border border-gray-300 rounded-md p-3 text-sm h-24 resize-none focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>
            </div>
          )}

          <div className="mx-auto rounded-lg p-1">
            {feedbacks.length === 0
              ? "No Feedbacks"
              : [...feedbacks].reverse().map((feedback, index) => {
                  // Get the rating, if available (you can replace it with the actual property from your feedback object)
                  const rating = feedback.ratings || 0; // Assuming the rating is a number from 0 to 5

                  // Calculate the number of full stars and half stars
                  const fullStars = Math.floor(rating); // Get full stars
                  const halfStar = rating % 1 >= 0.5; // Check if there's a half star

                  return (
                    <div
                      key={index}
                      className="bg-gray-100 p-6 rounded-lg mb-6 shadow-lg border border-gray-200"
                    >
                      <div className="flex items-center justify-between mb-4">
                        {/* Project Name */}
                        <h3 className="text-xl font-semibold text-gray-800 font-roboto">
                          {feedback.projectName}
                        </h3>
                        {/* Render stars in the right corner */}
                        <div className="flex items-center">
                          {[...Array(5)].map((_, starIndex) => {
                            if (starIndex < fullStars) {
                              return (
                                <FaStar
                                  key={starIndex}
                                  className="text-yellow-500"
                                />
                              );
                            } else if (starIndex === fullStars && halfStar) {
                              return (
                                <FaStarHalfAlt
                                  key={starIndex}
                                  className="text-yellow-500"
                                />
                              );
                            }
                            return (
                              <FaStar
                                key={starIndex}
                                className="text-gray-300"
                              />
                            );
                          })}
                        </div>
                      </div>
                      <p className="text-sm text-gray-600 mt-[-15px]">
                        {formatDate(feedback.startDate)} -{" "}
                        {formatDate(feedback.endDate)}
                      </p>
                      <p className="mt-4 text-gray-800 leading-relaxed font-roboto">
                        {feedback.review}
                      </p>
                      <p className="text-sm text-gray-600 mt-3 flex items-center gap-2">
                        <FaUserAlt className="text-gray-500" />
                        <span className="text-gray-500 font-roboto">
                          Feedback by:
                        </span>
                        <span className="text-gray-500 font-roboto">
                          {feedback.givenBy}
                        </span>
                      </p>
                    </div>
                  );
                })}
          </div>
        </div>
      </div>
      <ToastContainer />
    </form>
  );
};

export default ProjectAllocation;
