import React, { useEffect, useState } from "react";
import "../style/OccupationExperience.css";
import { FaPenSquare } from "react-icons/fa";
import proExpClient from "../AxiosClient/ProfessionalExperienceClient";
import { useSelector } from "react-redux";
import feedbackRatingClient from "../AxiosClient/FeedbackRatingClient";
import { toast, ToastContainer } from "react-toastify";
import { RiAddCircleLine } from "react-icons/ri";
import profileStatusClient from "../AxiosClient/profileStatusClient";
import ProfileDetails from "../AxiosClient/ProfileDetails";
import InterviewForm from "../components/InterviewForm";
import RoleVerticledrop from "./RoleVerticledrop";
import InterviewFeedbackList from "./InterviewFeedbackList";
import roleVerticalClient from "../AxiosClient/roleVerticalClient";
import axios from "axios";

const OccupationExperience = () => {
  const [showForm, setShowForm] = useState(false);
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const [selectedData, setSelectedData] = useState({ role: "", vertical: "" });

  const handleSelectionChange = (data) => {
    setSelectedData(data);
    console.log("Selected Data:", data); //  Logs the selected role & vertical

    // Update formData with role and vertical
    setFormData((prev) => ({
      ...prev,
      role: data.role,
      vertical: data.vertical,
    }));
  };

  const handleApprove = async () => {
    try {
      // Fetch profile data using the GET API
      const profileResponse = await ProfileDetails.get(`/${currentProfileId}`);
      if (profileResponse.status !== 200 || !profileResponse.data) {
        toast.error("Failed to fetch profile details.");
        return;
      }

      const profileData = profileResponse.data;
      console.log("Profile Data:", profileData);

      // console.log("Profile Data:", profileData);
      // console.log("Primary Email:", profileData.primaryEmail);
      // console.log("Contact Number:", profileData.contactNo);
      // console.log("Profession:", profileData.professionalExperience.profession);
      // console.log("Qualification:", profileData.professionalExperience.qualification);
      // console.log("Experience Years:", profileData.professionalExperience.experienceYears);
      // console.log("Relevant Experience:", profileData.professionalExperience.relevantExperience);
      // console.log("Primary Discipline:", profileData.professionalExperience.primaryDisciplines);
      // console.log("Secondary Discipline:", profileData.professionalExperience.secondaryDisciplines);

      // Validate required fields
      if (
        !profileData.primaryEmail ||
        !profileData.contactNo ||
        // !profileData.professionalExperience.profession ||
        // !profileData.professionalExperience.qualification ||
        !profileData.professionalExperience.experienceYears ||
        !profileData.professionalExperience.relevantExperience ||
        !profileData.professionalExperience.primaryDisciplines 
        // !profileData.professionalExperience.secondaryDisciplines
      ) {
        toast.error(
          "All professional information, including email and contact, are required to approve the profile."
        );
        return;
      }
      //if the profile is already approved it will show a error massage
      else if (profileData.profileStatus.isApproved) {
        toast.error("This Profile is already approved.");
        return;
      }

      // Proceed to update the profile status
      const profileStatusDto = { isApproved: true };
      const response = await profileStatusClient.put(
        `/${currentProfileId}`,
        profileStatusDto
      );

      if (response.status === 200) {
        setFormData((prevData) => ({
          ...prevData,
          isApproved: true, // Update local state
        }));
        toast.success("Profile has been successfully approved!");
      } else {
        toast.error("Failed to approve the profile. Please try again.");
      }
    } catch (error) {
      console.error("Error processing profile approval:", error);
      toast.error(
        error.response?.data?.message ||
          "An error occurred while approving the profile."
      );
    }
  };

  const [touchedFields, setTouchedFields] = useState({
    primaryDisciplines: false,
  });

  const [formData, setFormData] = useState({
    // profession: null,
    qualification: null,
    experienceYears: 7.25,
    relevantExperience: 7.8,
    feedback: "An outstanding professional with exceptional expertise...",
    rating: 5,
    primaryDisciplines: [],
    secondaryDisciplines: [],
    preferences: [],
    role: "",
    vertical: "",
    // feedbacks: [],
  });

  const [platforms, setPlatforms] = useState([]);

  const handleAddPlatform = () => {
    if (platforms.length < 3) {
      setPlatforms([...platforms, { platform: "", url: "" }]);
    } else {
      toast.error("You can only add up to 3 platforms.", {
        position: "top-right",
        autoClose: 3000,
      });
    }
  };

  const handleRemovePlatform = (index) => {
    const updatedPlatforms = platforms.filter((_, i) => i !== index);
    setPlatforms(updatedPlatforms);
  };

  const handlePlatformChange = (index, field, value) => {
    const updatedPlatforms = platforms.map((item, i) =>
      i === index ? { ...item, [field]: value } : item
    );
    setPlatforms(updatedPlatforms);
  };

  // const [feedbacks, setFeedbacks] = useState(formData.feedbacks || []);

  // const handleFeedbackChange = (index, field, value) => {
  //   const updatedFeedbacks = [...feedbacks];
  //   updatedFeedbacks[index] = { ...updatedFeedbacks[index], [field]: value };
  //   setFeedbacks(updatedFeedbacks);
  //   setFormData({ ...formData, feedbacks: updatedFeedbacks });
  // };

  // const handleAddFeedback = () => {
  //   setFeedbacks([...feedbacks, {
  //     projectName: "",
  //     startDate: "",
  //     endDate: "",
  //     ratings: 1,
  //     review: "",
  //     givenBy: ""
  //   }]);
  // };

  // const handleRemoveFeedback = (index) => {
  //   const updatedFeedbacks = feedbacks.filter((_, i) => i !== index);
  //   setFeedbacks(updatedFeedbacks);
  //   setFormData({ ...formData, feedbacks: updatedFeedbacks });
  // };

  //09/01
  // const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const isViewOnly = useSelector((state) => state.popup.isReadOnly);
  const isEditOnly = useSelector((state) => state.editProfile.isEdit);
  const fetchProExp = async () => {
    const res = await proExpClient
      .get(`/${currentProfileId}`)
      .then((response) => response)
      .catch((error) => {
        console.log(
          "There is some errror while fetching professional experience for ID : ",
          error
        );
      });
    const { id, profileId, ...fetchedData } = res.data;

    setFormData((prevFormData) => ({
      ...prevFormData,
      ...fetchedData,
    }));
    console.log(res.data);
  };

  const capitalizeFirstLetter = (skill) => {
    if (!skill) return skill;
    else if (skill.length < 4) return skill.toUpperCase();
    return skill.charAt(0).toUpperCase() + skill.slice(1);
  };

  const fetchFeedback = async () => {
    try {
      const res = await feedbackRatingClient.get(`/${currentProfileId}`);

      if (res && res.data) {
        // const { feedback, rating, preferences, feedbacks, ...restData } = res.data;
        const { feedback, rating, preferences, ...restData } = res.data;

        // Update formData with feedback and rating while preserving other fields
        setFormData((prevFormData) => ({
          ...prevFormData,
          feedback: feedback || "", // Use empty string if feedback is missing
          rating: rating ? rating.toString() : "", // Convert rating to string
          preferences: preferences || [],
          // feedbacks: feedbacks || [],
        }));
        setPlatforms(() => preferences);
        // setFeedbacks(() => feedbacks);
      }
      console.log("Feedback");
      console.log(res.data);
    } catch (error) {
      console.error("There is some error while fetching feedback: ", error);
    }
  };

  useEffect(() => {
    fetchProExp();
    fetchFeedback();
  }, []);

  useEffect(() => {
    setFormData((prev) => ({
      ...prev,
      preferences: platforms,
      // feedbacks: feedbacks,
    }));
    // }, [platforms, feedbacks]);
  }, [platforms]);

  //26/12
  const [isFocused, setIsFocused] = useState({
    primaryDisciplines: false,
    secondaryDisciplines: false,
  });

  const [isHovered, setIsHovered] = useState({
    primaryDisciplines: false,
    secondaryDisciplines: false,
  });

  const [newPrimarySkill, setNewPrimarySkill] = useState("");
  const [newSecondarySkill, setNewSecondarySkill] = useState("");

  const [formErrors, setFormErrors] = useState({
  //  profession: "",
    qualification: "",
    experienceYears: "",
    relevantExperience: "",
    feedback: "",
    rating: "",
    primaryDiscipline: "",
    secondaryDiscipline: "",
    preferences: "",
    // feedbacks: "",
  });

  //27/12
  const validate = (name, value) => {
    let error = "";
    switch (name) {
      // case "profession":
      //   // value = value.replace(/[^a-zA-Z\s-]/g, "");

      //   if (value == null || value.trim() === "") {
      //     error = "Profession is required";
      //   } else if (!/^[a-zA-Z\s-]+$/.test(value)) {
      //     error = "Profession can only contain alphabets, spaces, and dashes";
      //   }

      //   break;

      case "qualification":
        if (value == null || value.trim() === "") {
          error = "Qualification is required";
        } else if (!/^[a-zA-Z.,\s]+$/.test(value)) {
          error =
            "Qualification can only contain alphabets, commas, and periods";
        }
        break;

      case "primaryDisciplines":
        if (formData.primaryDisciplines.length == 0) {
          error = "Primary skills should contain at least 1 skill.";
        }

      default:
        break;
    }

    return error;
  };

  //26/12
  const handleSkillAdd = (type, newSkill, setNewSkill) => {
    if (newSkill.trim() !== "") {
      setFormData((prev) => ({
        ...prev,
        [type]: [...prev[type], newSkill.trim()],
      }));
      setNewSkill("");
    }
  };

  const removeSkill = (type, index) => {
    setFormData((prev) => ({
      ...prev,
      [type]: prev[type].filter((_, i) => i !== index),
    }));
  };

  const ratingOptions = [
    { key: "", value: "Select Rating" },
    { key: 1, value: "1-Poor" },
    { key: 2, value: "2-Average" },
    { key: 3, value: "3-Good" },
    { key: 4, value: "4-Very Good" },
    { key: 5, value: "5-Outstanding" },
  ];

  const [dropdownData, setDropdownData] = useState({
    experienceYears: 0,
    experienceMonths: 0,
    relevantExperienceYears: 0,
    relevantExperienceMonths: 0,
  });

  // Initialize dropdownData based on float values
  useEffect(() => {
    const parseFloatToDropdown = (floatVal) => {
      const years = Math.floor(floatVal);
      const months = Math.round((floatVal - years) * 12);
      return { years, months };
    };

    setDropdownData({
      experienceYears: parseFloatToDropdown(formData.experienceYears).years,
      experienceMonths: parseFloatToDropdown(formData.experienceYears).months,
      relevantExperienceYears: parseFloatToDropdown(formData.relevantExperience)
        .years,
      relevantExperienceMonths: parseFloatToDropdown(
        formData.relevantExperience
      ).months,
    });
  }, [formData.experienceYears, formData.relevantExperience]);

  const handleDropdownChange = (e) => {
    const { name, value } = e.target;

    setDropdownData((prev) => ({
      ...prev,
      [name]: Number(value),
    }));

    // Update experience or relevantExperience in formData
    const updatedValues = { ...dropdownData, [name]: Number(value) };
    if (name.startsWith("experience")) {
      const updatedExperience =
        updatedValues.experienceYears + updatedValues.experienceMonths / 12;
      setFormData((prev) => ({
        ...prev,
        experienceYears: updatedExperience,
      }));
    } else if (name.startsWith("relevantExperience")) {
      const updatedRelevantExperience =
        updatedValues.relevantExperienceYears +
        updatedValues.relevantExperienceMonths / 12;
      setFormData((prev) => ({
        ...prev,
        relevantExperience: updatedRelevantExperience,
      }));
    }
  };

  // Handle Input and Dropdown Changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    const error = validate(name, value);
    setFormData({ ...formData, [name]: value });
    setFormErrors({ ...formErrors, [name]: error });
  };

  //26/12

//   const handleSubmit = async (event) => {
//     console.log("handleSubmit clicked!");
//     event.preventDefault();
//     const errors = {};
//     let hasErrors = false;
//     Object.keys(formData).forEach((key) => {
//       const error = validate(key, formData[key]);
//       if (error) {
//         hasErrors = true;
//         errors[key] = error;

//         toast.error(`${error}`, {
//           position: "top-right",
//           autoClose: 3000,
//           hideProgressBar: false,
//           closeOnClick: true,
//           pauseOnHover: true,
//           draggable: true,
//           progress: undefined,
//         });
//       }
//     });

//     setFormErrors(errors);

//     if (!hasErrors) {
//       try {
//         console.log("Checking submission data");
//         console.log(formData);

//         const professionalData = {
//           primaryDisciplines: formData.primaryDisciplines,
//           secondaryDisciplines: formData.secondaryDisciplines,
// //          profession: formData.profession,
//           qualification: formData.qualification,
//           experienceYears: formData.experienceYears,
//           relevantExperience: formData.relevantExperience,
//           preferences: formData.preferences,
         
//         };

//         const feedbackData = {
//           feedback: formData.feedback,
//           rating: formData.rating,
//           preferences: formData.preferences,
//           // feedbacks: formData.feedbacks,
//         };

//         const roleVerticalData={
//           roleId: formData.role, 
//           verticalId: formData.vertical,
          

//         }
//           console.log(roleVerticalData);
//          /// console.log(verticalId);

//         //Send the 2 PUT requests
//         const [professionalRes, feedbackRes,roleVerticalRes] = await Promise.all([
//           proExpClient.put(`/${currentProfileId}`, professionalData),
//           feedbackRatingClient.put(`/${currentProfileId}`, feedbackData),
//           roleVerticalClient.post(`/${currentProfileId}`, roleVerticalData)
//         ]
      
//       );
      


//         if (professionalRes.status === 200 && feedbackRes.status === 200 && roleVerticalRes.status === 200) {
//           toast.success("Form updated successfully!", {
//             position: "top-right",
//             autoClose: 3000,
//             hideProgressBar: false,
//             closeOnClick: true,
//             pauseOnHover: true,
//             draggable: true,
            
//           });
//         }
//       } catch (error) {
//         console.error("Error updating form:", error);
//         toast.error("Failed to update the form. Please try again later.", {
//           position: "top-right",
//           autoClose: 3000,
//           hideProgressBar: false,
//           closeOnClick: true,
//           pauseOnHover: true,
//           draggable: true,
//         });
//       }
//     } else {
//       console.log(
//         "Occupation and Experience Form has errors. Please fix them before submitting."
//       );
//     }
//   };

const handleSubmit = async (event) => {
  console.log("handleSubmit clicked!");
  event.preventDefault();

  const errors = {};
  let hasErrors = false;

  Object.keys(formData).forEach((key) => {
    const error = validate(key, formData[key]);
    if (error) {
      hasErrors = true;
      errors[key] = error;

      toast.error(`${error}`, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
    }
  });

  setFormErrors(errors);

  if (!hasErrors) {
    try {
      console.log("Checking submission data");
      console.log(formData);

      const professionalData = {
        primaryDisciplines: formData.primaryDisciplines,
        secondaryDisciplines: formData.secondaryDisciplines,
        qualification: formData.qualification,
        experienceYears: formData.experienceYears,
        relevantExperience: formData.relevantExperience,
        preferences: formData.preferences,
      };

      const feedbackData = {
        feedback: formData.feedback,
        rating: formData.rating,
        preferences: formData.preferences,
      };

      const roleVerticalData = {
        roleId: formData.role,
        verticalId: formData.vertical,
      };

      console.log("RoleVertical Data:", roleVerticalData);

      // Send the 2 PUT requests and 1 POST request
      const [professionalRes, feedbackRes, roleVerticalRes] = await Promise.all([
        proExpClient.put(`/${currentProfileId}`, professionalData),
        feedbackRatingClient.put(`/${currentProfileId}`, feedbackData),
        axios.post(`http://localhost:8081/profiles/${currentProfileId}`, roleVerticalData) // Ensure correct API URL
      ]);

      if (
        professionalRes.status === 200 &&
        feedbackRes.status === 200 &&
        roleVerticalRes.status === 200
      ) {
        toast.success("Form updated successfully!", {
          position: "top-right",
          autoClose: 3000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        });
      }
    } catch (error) {
      console.error("Error updating form:", error);
      toast.error("Failed to update the form. Please try again later.", {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
    }
  } else {
    console.log(
      "Occupation and Experience Form has errors. Please fix them before submitting."
    );
  }
};




  return (
    <form className="occupation-experience-form" onSubmit={handleSubmit}>
      <h2 className="form-title">Occupation and Experience</h2>
      <div className="border border-gray-300 rounded-md p-4 w-full max-w-3xl bg-[#EFF4F6]">
        {/* Primary and Secondary Skill  */}
        <div className="grid grid-cols-2 gap-4">
          {/* Primary Skills  */}
          <div className="">
            <label
              className={`block text-sm font-medium text-gray-700 mb-2  ${
                formData.primaryDisciplines.length > 0 ||
                isFocused.primaryDisciplines ||
                isHovered.primaryDisciplines
                  ? "active"
                  : ""
              }${
                touchedFields.primaryDisciplines &&
                formData.primaryDisciplines.length === 0
                  ? "error"
                  : ""
              }`}
            >
              Primary Skill
            </label>
            <div
              className="flex items-center flex-wrap border border-gray-300 rounded-md p-0.5 bg-white"
              onMouseEnter={() =>
                setIsHovered({ ...isHovered, primaryDisciplines: true })
              }
              onMouseLeave={() =>
                setIsHovered({ ...isHovered, primaryDisciplines: false })
              }
            >
              {formData.primaryDisciplines.map((skill, index) => (
                <div
                  className={`flex items-center bg-blue-100 text-blue-800 px-2 py-0.5 rounded-md m-1 ${
                    isViewOnly ? "disabled" : ""
                  }`}
                  key={index}
                >
                  <span className="text">{capitalizeFirstLetter(skill)}</span>
                  {!isViewOnly && (
                    <span
                      className="close"
                      onClick={() =>
                        !isViewOnly && removeSkill("primaryDisciplines", index)
                      }
                    >
                      &times;
                    </span>
                  )}
                </div>
              ))}
              {!isViewOnly && (
                <input
                  type="text"
                  value={newPrimarySkill}
                  onChange={(e) => setNewPrimarySkill(e.target.value)}
                  // onKeyDown={handleKeyDown}
                  disabled={
                    isViewOnly || formData.primaryDisciplines.length == 6
                  }
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      e.preventDefault(); // Prevent form submission
                      handleSkillAdd(
                        "primaryDisciplines",
                        newPrimarySkill,
                        setNewPrimarySkill
                      );
                    }
                  }}
                  className="mt-2 p-2 border bg-white border-gray-300 rounded-md"
                  placeholder={
                    formData.primaryDisciplines.length > 0 ||
                    isFocused.primaryDisciplines ||
                    isHovered.primaryDisciplines
                      ? "Add skill"
                      : ""
                  }
                  onFocus={() => {
                    setIsFocused({ ...isFocused, primaryDisciplines: true });
                    setTouchedFields({
                      ...touchedFields,
                      primaryDisciplines: true,
                    }); // Mark as touched
                  }}
                  onBlur={() =>
                    setIsFocused({ ...isFocused, primaryDisciplines: false })
                  }
                />
              )}
            </div>
            {/* Validation error message */}
            {touchedFields.primaryDisciplines &&
              formData.primaryDisciplines.length === 0 && (
                <span className="error-message">.</span>
              )}
          </div>

          {/* Secondary Skills  */}
          <div className="">
            <label
              className={`block text-sm font-medium text-gray-700 mb-2 ${
                formData.secondaryDisciplines.length > 0 ||
                isFocused.secondaryDisciplines ||
                isHovered.secondaryDisciplines
                  ? "active"
                  : ""
              }`}
            >
              Secondary Skill
            </label>
            <div
              className="flex items-center flex-wrap border border-gray-300 rounded-md p-0.5 bg-white"
              onMouseEnter={() =>
                setIsHovered({ ...isHovered, secondaryDisciplines: true })
              }
              onMouseLeave={() =>
                setIsHovered({ ...isHovered, secondaryDisciplines: false })
              }
            >
              {formData.secondaryDisciplines.map((skill, index) => (
                <div
                  className={`flex items-center bg-blue-100 text-blue-800 px-2 py-0.5 rounded-md m-1${
                    isViewOnly ? "disabled" : ""
                  }`}
                  key={index}
                >
                  <span className="text">{capitalizeFirstLetter(skill)}</span>
                  {!isViewOnly && (
                    <span
                      className="close"
                      onClick={() =>
                        !isViewOnly &&
                        removeSkill("secondaryDisciplines", index)
                      }
                    >
                      &times;
                    </span>
                  )}
                </div>
              ))}
              {!isViewOnly && (
                <input
                  type="text"
                  value={newSecondarySkill}
                  // onKeyDown={handleKeyDown}
                  onChange={(e) => setNewSecondarySkill(e.target.value)}
                  disabled={isViewOnly}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      e.preventDefault(); // Prevent form submission
                      handleSkillAdd(
                        "secondaryDisciplines",
                        newSecondarySkill,
                        setNewSecondarySkill
                      );
                    }
                  }}
                  className="mt-1 p-2 border bg-white border-gray-300 rounded-md"
                  placeholder={
                    formData.secondaryDisciplines.length > 0 ||
                    isFocused.secondaryDisciplines
                      ? "Add skill"
                      : ""
                  }
                  onFocus={() =>
                    setIsFocused({ ...isFocused, secondaryDisciplines: true })
                  }
                  onBlur={() =>
                    setIsFocused({ ...isFocused, secondaryDisciplines: false })
                  }
                />
              )}
            </div>
          </div>
        </div>

        {/*Role and Verticle */}
        <RoleVerticledrop onSelectionChange={handleSelectionChange} />

        <div className="">
          {/* Profession */}
          {/* <div className="form-group profession">
          <input
            type="text"
            name="profession"
            value={formData.profession || ""}
            onChange={handleChange}
            placeholder="Enter Profession"
            disabled = {isViewOnly}
            className={`profession-input ${formData.profession ? "has-value" : "has-placeholder"}`}
          />
          <label className={formErrors.profession ? "error" : ""}>
            Profession
          </label>
          {!isViewOnly && <FaPenSquare className="icon" />}
        </div> */}

          {/* Qualification */}
          <div className="col-span-2 flex gap-4 my-4">
            <div className="w-1/2">
              <label
                className={`block text-sm font-medium text-gray-700 mb-2${
                  formErrors.qualification ? "error" : ""
                }`}
              >
                Qualification
              </label>
              <input
                type="text"
                name="qualification"
                value={formData.qualification || ""}
                placeholder="Enter Qualification"
                onChange={handleChange}
                disabled={isViewOnly}
                className={`mt-1 block w-full p-2 bg-white border border-gray-300 rounded-md ${
                  formData.qualification ? "has-value" : "has-placeholder"
                }`}
              />

              {!isViewOnly && <FaPenSquare className="icon" />}
            </div>

            {/* Experience */}
            <div className="w-1/2">
              <label
                className={`block text-sm font-medium text-gray-700 mb-2${
                  dropdownData.experienceYears >= 0 ? "active" : ""
                }`}
              >
                Experience:
              </label>
              <div className="flex gap-2">
                <select
                  name="experienceYears"
                  value={dropdownData.experienceYears}
                  onChange={handleDropdownChange}
                  disabled={isViewOnly}
                  className="mt-1 block w-1/2 p-2 border border-gray-300 rounded-sm "
                >
                  <option value=""></option>
                  {[...Array(36).keys()].map((year) => (
                    <option key={year} value={year}>
                      {year} Year{year !== 1 ? "s" : ""}
                    </option>
                  ))}
                </select>
                <select
                  name="experienceMonths"
                  value={dropdownData.experienceMonths}
                  onChange={handleDropdownChange}
                  disabled={isViewOnly}
                  className="mt-1 block w-1/2 p-2 border border-gray-300 rounded-sm "
                >
                  <option value=""></option>
                  {[...Array(12).keys()].map((month) => (
                    <option key={month} value={month}>
                      {month} Month{month !== 1 ? "s" : ""}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>
        </div>

        {/* Relevant Experience */}
        <div className="grid grid-cols-2 gap-4 my-4">
          <div className="col-span-1">
            <label
              className={`block text-sm font-medium text-gray-700 mb-2${
                dropdownData.relevantExperienceYears >= 0 ? "active" : ""
              }`}
            >
              Relevant Experience:
            </label>
            <div className="w-full gap-2">
              <div className=" flex gap-2">
                <select
                  name="relevantExperienceYears"
                  value={dropdownData.relevantExperienceYears}
                  onChange={handleDropdownChange}
                  disabled={isViewOnly}
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                >
                  {[...Array(36).keys()].map((year) => (
                    <option key={year} value={year}>
                      {year} Year{year !== 1 ? "s" : ""}
                    </option>
                  ))}
                </select>
                <select
                  name="relevantExperienceMonths"
                  value={dropdownData.relevantExperienceMonths}
                  onChange={handleDropdownChange}
                  disabled={isViewOnly}
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                >
                  {[...Array(12).keys()].map((month) => (
                    <option key={month} value={month}>
                      {month} Month{month !== 1 ? "s" : ""}
                    </option>
                  ))}
                </select>
              </div>
              {/* {platforms.map((platform, index) => (
                <div className="flex col-span-1 gap-2" key={index}>
                  <div className="flex flex-col gap-2">
                    <input
                      type="text"
                      placeholder="Platform"
                      value={platform.platform}
                      onChange={(e) =>
                        handlePlatformChange(index, "platform", e.target.value)
                      }
                      disabled={isViewOnly}
                      className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                    />
                  </div>
                  <div className="flex gap-2">
                    {isViewOnly && platform.url ? (
                      // Render clickable link if in view-only mode
                      <a
                        href={platform.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="url-link"
                      >
                        {platform.url}
                      </a>
                    ) : (
                      <input
                        type="url"
                        placeholder="URL"
                        value={platform.url}
                        onChange={(e) =>
                          handlePlatformChange(index, "url", e.target.value)
                        }
                        disabled={isViewOnly}
                        className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                      />
                    )}
                  </div>
                  {!isViewOnly && (
                    <button
                      type="button"
                      onClick={() => handleRemovePlatform(index)}
                      className="px-2 py-1 text-red-600 rounded-md text-sm"
                    >
                      x
                    </button>
                  )}
                </div>
              ))} */}
            </div>
          </div>
          <div className="col-span-1 gap-2 ">
            <div className="w-full flex justify-between ">
              <label className=" block text-sm font-medium text-gray-700">
                Reference
              </label>
              {platforms.length > 0 && platforms.length < 3 && !isViewOnly && (
                <button
                  type="button"
                  onClick={handleAddPlatform}
                  disabled={isViewOnly}
                  className="px-3 bg-blue-400 border  border-gray-300 rounded-2xl text-white-400 hover:bg-gray-100"
                >
                  + Add
                </button>
              )}
            </div>
            <div className="flex flex-col gap-2 mt-0">
              {platforms.map((platform, index) => (
                <div className="flex gap-2" key={index}>
                  <div className="flex flex-col gap-2">
                    <input
                      type="text"
                      placeholder="Platform"
                      value={platform.platform}
                      onChange={(e) =>
                        handlePlatformChange(index, "platform", e.target.value)
                      }
                      disabled={isViewOnly}
                      className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                    />
                  </div>
                  <div className="flex gap-2">
                    {isViewOnly && platform.url ? (
                      // Render clickable link if in view-only mode
                      <a
                        href={platform.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="url-link"
                      >
                        {platform.url}
                      </a>
                    ) : (
                      <input
                        type="url"
                        placeholder="URL"
                        value={platform.url}
                        onChange={(e) =>
                          handlePlatformChange(index, "url", e.target.value)
                        }
                        disabled={isViewOnly}
                        className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                      />
                    )}
                  </div>
                  {!isViewOnly && (
                    <button
                      type="button"
                      onClick={() => handleRemovePlatform(index)}
                      className="px-2 py-1 text-red-600 rounded-md text-sm"
                    >
                      x
                    </button>
                  )}
                </div>
              ))}
              {platforms.length === 0 && !isViewOnly && (
                <button
                  type="button"
                  onClick={handleAddPlatform}
                  disabled={isViewOnly}
                  className="w-1/2 px-3 py-1  border  border-gray-300 rounded-2xl text-blue-400 hover:bg-gray-100"
                >
                  + Add Reference
                </button>
              )}
            </div>
          </div>
        </div>

        {/* Preferences Section */}
        {/* <div className="w-full flex items-center gap-2 ">
          <label className=" block text-sm font-medium text-gray-700">
            Reference
          </label>
          <div className="flex items-center gap-2 mt-1">
            {platforms.map((platform, index) => (
              <div className="flex gap-2" key={index}>
                <div className="flex flex-col gap-2">
                  <input
                    type="text"
                    placeholder="Platform"
                    value={platform.platform}
                    onChange={(e) =>
                      handlePlatformChange(index, "platform", e.target.value)
                    }
                    disabled={isViewOnly}
                    className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                  />
                </div>
                <div className="flex gap-2">
                  {isViewOnly && platform.url ? (
                    // Render clickable link if in view-only mode
                    <a
                      href={platform.url}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="url-link"
                    >
                      {platform.url}
                    </a>
                  ) : (
                    <input
                      type="url"
                      placeholder="URL"
                      value={platform.url}
                      onChange={(e) =>
                        handlePlatformChange(index, "url", e.target.value)
                      }
                      disabled={isViewOnly}
                      className="border border-gray-300 rounded-md p-2 w-full text-sm bg-white"
                    />
                  )}
                </div>
                {!isViewOnly && (
                  <button
                    type="button"
                    onClick={() => handleRemovePlatform(index)}
                    className="px-2 py-1 text-red-600 rounded-md text-sm"
                  >
                    x
                  </button>
                )}
              </div>
            ))}
            {platforms.length < 3 && !isViewOnly && (
              <button
                type="button"
                onClick={handleAddPlatform}
                disabled={isViewOnly}
                className="px-3 py-1  border  border-gray-300 rounded-2xl text-blue-400 hover:bg-gray-100"
              >
                + Add Reference
              </button>
            )}
          </div>
        </div> */}
      </div>

      {/* Feedbacks Section */}
      {/* <div className="mx-auto bg-gray-100 rounded-lg p-4">
      <h2 className="text-lg font-semibold text-gray-800 mb-2">Feedbacks</h2>
      {feedbacks.map((feedback, index) => (
        <div className="bg-white p-2 rounded-lg mb-2 shadow-sm border border-black" key={index}>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 mb-2">
            <div className="relative">
              <label className="text-black text-sm font-normal">Project Name</label>
              <input
                type="text"
                placeholder="Enter Project Name"
                value={feedback.projectName}
                onChange={(e) => handleFeedbackChange(index, "projectName", e.target.value)}
                disabled={isViewOnly}
                className="w-full bg-white border border-black rounded-md p-1 text-sm focus:outline-none"
              />
            </div>
            <div className="relative">
              <label className="text-black text-sm font-normal">Start Date</label>
              <input
                type="date"
                value={feedback.startDate}
                onChange={(e) => handleFeedbackChange(index, "startDate", e.target.value)}
                disabled={isViewOnly}
                className="w-full bg-white border border-black rounded-md p-1 text-sm focus:outline-none"
              />
            </div>
            <div className="relative">
              <label className="text-black text-sm font-normal">End Date</label>
              <input
                type="date"
                value={feedback.endDate}
                onChange={(e) => handleFeedbackChange(index, "endDate", e.target.value)}
                disabled={isViewOnly}
                className="w-full bg-white border border-black rounded-md p-1 text-sm focus:outline-none"
              />
            </div>
          </div>
          <div className="relative mb-2">
            <label className="text-black text-sm font-normal">Rating</label>
            <select
              value={feedback.ratings}
              onChange={(e) => handleFeedbackChange(index, "ratings", Number(e.target.value))}
              disabled={isViewOnly}
              className="w-full bg-white border border-black rounded-md p-1 text-sm focus:outline-none"
            >
              {[1, 2, 3, 4, 5].map((rating) => (
                <option key={rating} value={rating}>{rating} Stars</option>
              ))}
            </select>
          </div>
          <div className="relative mb-2">
            <label className="text-black text-sm font-normal">Feedback</label>
            <textarea
              placeholder="Write your Feedback here"
              value={feedback.review}
              onChange={(e) => handleFeedbackChange(index, "review", e.target.value)}
              disabled={isViewOnly}
              className="w-full bg-white border border-black rounded-md p-1 text-sm h-16 resize-none focus:outline-none"
            />
          </div>
          <div className="relative mb-2">
            <label className="text-black text-sm font-normal">Mention Reviewer</label>
            <input
              type="text"
              placeholder="Enter Reviewer Name"
              value={feedback.givenBy}
              onChange={(e) => handleFeedbackChange(index, "givenBy", e.target.value)}
              disabled={isViewOnly}
              className="w-full bg-white border border-black rounded-md p-1 text-sm focus:outline-none"
            />
          </div>
          {!isViewOnly && (
            <button 
              type="button" 
              onClick={() => handleRemoveFeedback(index)} 
              className="mt-2 bg-red-500 text-white px-2 py-1 rounded-md shadow-sm hover:bg-red-600 transition duration-200 text-sm"
            >
              Remove Feedback
            </button>
          )}
        </div>
      ))}
      {!isViewOnly && (
        <button type="button" onClick={handleAddFeedback} className="mt-2 bg-blue-500 text-white px-3 py-1 rounded-md shadow-sm hover:bg-blue-600 transition duration-200 text-sm">
          Add Feedback
        </button>
      )}
</div> */}
     

     <div className="p-4">
      {/* Add Interview Feedback Button */}
      {!showForm && (
        <button
          type="button"
          onClick={() => setShowForm(true)}
          className="px-4 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
        >
          Add Interview Feedback
        </button>
      )}

      {/* Show InterviewForm when button is clicked */}
      {showForm && (
        <div className="mt-4 p-6 bg-white shadow-lg rounded-lg border border-gray-200">
          <InterviewForm profileId={currentProfileId} setShowForm={setShowForm} />
         
        </div>
      )}

      {/* Feedback List (Only shown when form is closed) */}
      {!showForm && <InterviewFeedbackList profileId={currentProfileId} />}
    </div>

      {/* Actions */}
      <div className="form-actions">
        {!isViewOnly && !isEditOnly && (
          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleApprove}
          >
            Approve
          </button>
        )}
        {!isViewOnly && (
          <button type="submit" className="btn btn-primary">
            Update
          </button>
        )}
      </div>
      <ToastContainer />
    </form>
  );
};

export default OccupationExperience;
