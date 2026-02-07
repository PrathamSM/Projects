import React, { useState, useEffect } from "react";
import axios from "axios";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


const API_BASE_URL = "http://localhost:8084/feedbacks"; 

const InterviewForm = ({ profileId, setShowForm }) => {
  const [formData, setFormData] = useState({
    projectName: "",
    clientName: "",

    interviewLevel: "",
    interviewer: "",
    interviewDate: null,
    result: "",
    rating: 0,
    feedback: "",
  });
  const [projects, setProjects] = useState([]);
  const [submittedFeedback, setSubmittedFeedback] = useState([]);
  const [selectedProject, setSelectedProject] = useState();
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

  // Fetch Feedback when Component Mounts
  useEffect(() => {
    if (profileId) {
      fetchFeedback(profileId);
    }
  }, [profileId]);

  // Fetch Feedback by Profile ID
  const fetchFeedback = async (pId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/${pId}`);
      setSubmittedFeedback([response.data]);
    } catch (error) {
      console.error("Error fetching feedback:", error);
    }
  };

  const handleCancel = () => {
    setShowForm(false); // Close form and show feedback list again
  };
  // Handle Input Change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleDateChange = (date) => {
    setFormData({ ...formData, interviewDate: date });
  };


  const handleRating = (rating) => {
    setFormData((prev) => ({
      ...prev,
      rating: rating, // Ensuring it updates correctly
    }));
  };


  // Submit Feedback
  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("FormData before submission:", formData);
    const newFeedback = {
      clientName: formData.clientName,
      type: formData.interviewLevel,
      name: formData.projectName,
      interviewer: formData.interviewer,
      date: formData.interviewDate
        ? new Date(formData.interviewDate)
          .toLocaleDateString("en-GB")
          .replace(/\//g, "-")
        : null,
      feedbackComment: formData.feedback,
      result: formData.result,
      interviewRating: formData.rating || 0, // Include rating in the submission
    };

    try {
      // Fetch existing feedbacks from backend
      const response = await axios.get(`${API_BASE_URL}/${profileId}`);
      const existingFeedbacks = response.data?.interviewFeedbacks || [];

      // Append the new feedback
      const updatedFeedbacks = [...existingFeedbacks, newFeedback];

      // Send updated feedbacks list to backend
      await axios.put(`${API_BASE_URL}/${profileId}`, {
        interviewFeedbacks: updatedFeedbacks,
      });

      alert("Feedback added successfully!");
      fetchFeedback(profileId);
    } catch (error) {
      console.error("Error submitting feedback:", error);
    }
    setShowForm(false);
  };

  return (
    <>
      <div className="flex justify-between ">
        <h2 className="text-xl font-bold">Interview Details</h2>

        {/* Top Buttons */}
        <div className="flex justify-end space-x-4">
          <button type="button"
            onClick={handleCancel} className="px-4 bg-white border border-gray-300 rounded">
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            className="px-4  bg-blue-500 text-white rounded"
            type="submit"
          >
            Save
          </button>
        </div>
      </div>
      <div className="w-full mx-auto  p-6 shadow-lg bg-[#EFF4F6] border border-[#D9E3E9] rounded-lg ">
        {/* Interview Form */}

        <form onSubmit={handleSubmit}>
          <p className="mb-6"> Basic Details</p>
          <div className="grid grid-cols-2 gap-6 border-b-2 border-gray-300 ">
            {/* Project Selection Dropdown */}

            <div >
              <label className="block text-sm font-medium">
                Project Name<span className="text-red-500">*</span>
              </label>
              <select
                className="w-full p-3 border focus:outline-blue-500 bg-white"
                value={selectedProject?.projectId || ""}
                onChange={(e) => {
                  const project = projects.find(
                    (p) => p.projectId === e.target.value
                  );
                  setSelectedProject(project);
                  setFormData((prev) => ({
                    ...prev,
                    projectName: project?.projectName || "",
                  }));
                }}
              >
                <option className="w-full p-3" value="" disabled>
                  Select project
                </option>
                {projects.map((project) => (
                  <option key={project.projectId} value={project.projectId}>
                    {project.projectName} ({project.projectId})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium">
                Client Name
              </label>
              <input
                type="text"
                name="clientName"
                value={formData.clientName}
                onChange={handleChange}
                className="w-full p-2 border rounded"
                placeholder="Client Name"
              />
            </div>

            <div>
              <label className="block text-sm font-medium">
                Interview Level<span className="text-red-500">*</span>
              </label>

              <select
                name="interviewLevel"
                value={formData.interviewLevel}
                onChange={handleChange}
                className="w-full p-2 border rounded"
              >
                <option value="">Select level</option>
                <option value="L1">L1</option>
                <option value="L2">L2</option>
                <option value="L3">L3</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium">
                Interview taken by <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="interviewer"
                value={formData.interviewer}
                onChange={handleChange}
                className="w-full p-2 border rounded"
                placeholder="Interviewer name"
              />
            </div>

            <div>
              <label className="block text-sm font-medium ">
                Interview Date <span className="text-red-500">*</span>
              </label>
              <DatePicker
                selected={formData.interviewDate}
                onChange={handleDateChange}
                className="w-full p-2 border rounded mb-4"
                placeholderText="Select date"
              />
            </div>
          </div>

          <p className="mt-6 mb-6">Evaluation</p>

          <div className="flex justify-stretch">
            <div>
              <label className="block text-sm font-medium">Result</label>
              <select
                name="result"
                value={formData.result}
                onChange={handleChange}
                className="w-[280px] p-2 border rounded"
              >
                <option value="">Select result</option>
                <option value="Selected">Selected</option>
                <option value="Rejected">Rejected</option>
                <option value="On Hold">On Hold</option>
              </select>
            </div>

            {/* Rating Section */}
            <div className="mt-4">
              <label className="block text-sm font-medium">Rating</label>
              <div className="flex space-x-1">
                {[1, 2, 3, 4, 5].map((star) => (
                  <span
                    key={star}
                    className={`cursor-pointer text-xl ${star <= formData.rating
                        ? "text-yellow-500"
                        : "text-gray-400"
                      }`}
                    onClick={() => handleRating(star)}
                  >
                    ★
                  </span>
                ))}
              </div>
            </div>
          </div>

          <div className="mt-4">
            <label className="block text-sm font-medium">Write Feedback</label>
            <textarea
              name="feedback"
              value={formData.feedback}
              onChange={handleChange}
              className="w-full p-2 border rounded"
              placeholder="Write your feedback here..."
              rows="3"
            ></textarea>
          </div>
        </form>
      </div>

   
    </>
  );
};

export default InterviewForm;