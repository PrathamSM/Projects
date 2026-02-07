import axios from "axios";
import React, { useState, useEffect } from "react";
import { FaRegUser } from "react-icons/fa6";
import { RxCrossCircled } from "react-icons/rx";
import Select from "react-select";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


const customStyles = {
    control: (provided) => ({
      ...provided,
      border: "1px solid #ccc",
      padding: "4px",
    }),
  };

  const initialSkills = [
    { value: "Instructional Designer", label: "Instructional Designer" },
    { value: "Content Developer", label: "Content Developer" },
    { value: "Educational Consultant", label: "Educational Consultant" },
    { value: "Sales Coordinator (EdTech)", label: "Sales Coordinator (EdTech)" },
    { value: "Educational Technologist", label: "Educational Technologist" },
    { value: "Business Analyst", label: "Business Analyst" },
    { value: "add-new-skill", label: "➕ Add New Skill" },
  ];

  const globalAndIndianLanguages = [
    "English", "Spanish", "Mandarin", "French", "German",
    "Hindi", "Bengali", "Tamil", "Telugu", "Marathi",
  ];

const SMERequirements = ({ projectId, onSave }) => {
    // used this to check that the project Id is comming in this component correctly.
    console.log("Received projectId in Requirements:", projectId);

  const [formData, setFormData] = useState({
    projectId: projectId,
    certificateName: [],
    domainExperienceRequired: [],
    languageFluency: [],
    requiredSkills: [],
    numberOfSMEs: "",
    experienceLevel: "",
    newSkill: "",
    showNewSkillInput: false,
    newLanguage: "",
    showNewLanguageInput: false,
  });

  const experienceOptions = [
    "0-2 years",
    "2-4 years",
    "4-6 years",
    "6-8 years",
    "8-10 years",
    "10-12 years",
    "12-15 years",
    "15< years",
  ];

   useEffect(() => {
      console.log("Updated projectId in Requirement:", projectId);
    }, [projectId]);

    // Validate Form Data
    const validate = () => {
      const errors = {};
    
      if (!formData.languageFluency.length) errors.languageFluency = "Language Fluency is required.";
      if (!formData.requiredSkills.length) errors.requiredSkills = "At least one skill is required.";
      if (!formData.numberOfSMEs || isNaN(formData.numberOfSMEs) || formData.numberOfSMEs <= 0) 
        errors.numberOfSMEs = "Enter a valid number of SMEs.";
      if (!formData.experienceLevel) errors.experienceLevel = "Experience Level is required.";
      if (!formData.certificateName.length) errors.certificateName = "Add at least one certificate.";
      if (!formData.domainExperienceRequired) errors.domainExperienceRequired = "Domain Experience is required.";
    
      // If there are errors, show them as toast notifications
      if (Object.keys(errors).length > 0) {
        Object.values(errors).forEach((err) => toast.error(err));
        return false;
      }
      
      return true;
    };
    
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent page reload

    if (!validate()) return; //validating all the fields.

    if (!projectId) {
      toast.error("Project ID is missing!");
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8042/sme-requirements/create`,
        {
          ...formData,
        },
        {
          headers: {
            "Content-Type": "application/json", // Ensure proper request format
          },
        }
      );

      console.log("post response:", response.data); // i used this to see the submitted data
      toast.success("Requirement data Posted successfully!");
      onSave(); // use to save the form data and Move to the next step
    } catch (error) {
      console.error("Error posting Requirement:", error.response?.data || error.message);
      toast.error("Failed to post Requirement.");

    }
  };


  const [languageOptions, setLanguageOptions] = useState([
    ...globalAndIndianLanguages.map((lang) => ({ value: lang, label: lang })),
    { value: "add-new-language", label: "➕ Add New Language" },
  ]);
  
  const handleLanguageChange = (selectedOptions) => {
    const selectedValues = selectedOptions.map((option) => option.value);

    if (selectedValues.includes("add-new-language")) {
      setFormData((prev) => ({
        ...prev,
        showNewLanguageInput: true, // Show input field
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        languageFluency: selectedValues,
        showNewLanguageInput: false, // Hide input if "Add New Language" is not selected
      }));
    }
  };

  const addNewLanguage = () => {
    if (formData.newLanguage.trim() !== "") {
      const newLang = {
        value: formData.newLanguage.trim(),
        label: formData.newLanguage.trim(),
      };

      setLanguageOptions((prevOptions) => [
        ...prevOptions.slice(0, -1), // Remove "Add New Language" temporarily
        newLang,
        prevOptions[prevOptions.length - 1], // Re-add "Add New Language" at end
      ]);

      setFormData((prev) => ({
        ...prev,
        languageFluency: [...prev.languageFluency, formData.newLanguage.trim()], // Add new language to selected
        newLanguage: "",
        showNewLanguageInput: false, // Hide input field
      }));
    }
  };

  const [skillOptions, setSkillOptions] = useState(initialSkills);

  const handleSkillChange = (selectedOptions) => {
    const selectedValues = selectedOptions.map((option) => option.value);

    if (selectedValues.includes("add-new-skill")) {
      setFormData((prev) => ({
        ...prev,
        showNewSkillInput: true, // Show input field
      }));
    } else {
      setFormData((prev) => ({
        ...prev,
        requiredSkills: selectedValues,
        showNewSkillInput: false, // Hide input if "Add New Skill" is not selected
      }));
    }
  };

  const addNewSkill = () => {
    if (formData.newSkill.trim() !== "") {
      const newSkill = {
        value: formData.newSkill.trim(),
        label: formData.newSkill.trim(),
      };

      setSkillOptions((prevOptions) => [
        ...prevOptions.slice(0, -1), // Remove "Add New Skill" temporarily
        newSkill,
        prevOptions[prevOptions.length - 1], // Re-add "Add New Skill" at end
      ]);

      setFormData((prev) => ({
        ...prev,
        requiredSkills: [...prev.requiredSkills, formData.newSkill.trim()], // Add new skill to selected skills
        newSkill: "",
        showNewSkillInput: false, // Hide input field
      }));
    }
  };



  const TagInput = ({ placeholder, formData, setFormData, field }) => {
        const [inputValue, setInputValue] = useState("");
    
        // const handleKeyDown = (e) => {
        //   if (e.key === "Enter" && inputValue.trim() !== "") {
        //     setFormData({
        //       ...formData,
        //       [field]: [...(formData[field] || []), inputValue.trim()],
        //     });
        //     setInputValue("");
        //     e.preventDefault();
        //   }
        // };
    
        const handleKeyDown = (e) => {
          if (e.key === "Enter" && inputValue.trim() !== "") {
            if (formData[field].includes(inputValue.trim())) {
              toast.error("Duplicate entry not allowed!");
              return;
            }
            setFormData({
              ...formData,
              [field]: [...formData[field], inputValue.trim()],
            });
            setInputValue("");
            e.preventDefault();
          }
        };
        
        const removeTag = (index) => {
          setFormData({
            ...formData,
            [field]: formData[field].filter((_, i) => i !== index),
          });
        };
    
        return (
          <div className="w-full">
            <input
              type="text"
              placeholder={placeholder}
              className="border-b-2 border-blue-500 focus:outline-none p-2 w-full placeholder-blue-500"
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <div className="flex flex-wrap gap-2 border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition p-1">
               {(formData[field] || []).map((tag, index) => (
            <span 
          key={index} 
          className="bg-blue-500 text-white flex items-center px-3 py-1 rounded-xl"
          >
            {/* setting the limit of 10 characters if exceeds the the remaining characters will get hide */}
          <span className="truncate max-w-[100px]">
            {tag.length > 10 ? tag.slice(0, 10) + "..." : tag}
          </span>
          <button 
            className="ml-2 text-white font-bold" 
            onClick={() => removeTag(index)}
          >
            <RxCrossCircled />
          </button>
        </span>
      ))}
    </div>
  
   </div>
        );
      };

  return (
    <div className="p-6 space-y-6">
      <h3 className="text-lg font-semibold text-blue-500 mb-4 flex items-center gap-2">
        <FaRegUser />  Requirements
      </h3>

      <form id="SMERequirementForm" onSubmit={handleSubmit} className="space-y-8">
      <div className="flex gap-4">
      <div className="w-1/2">
      {/* <h2 className="text-lg font-semibold">Select Required Skills</h2> */}
      <Select
        isMulti
        options={skillOptions}
        value={skillOptions.filter((requiredSkills) => formData.requiredSkills.includes(requiredSkills.value))}
        onChange={handleSkillChange}
        styles={customStyles}
        placeholder="Select Required Skills"
      />
      </div>
      {/* Input field for adding a new skill */}
      {formData.showNewSkillInput && (
        <div className="w-1/2 flex gap-2 items-center">
          <input
            type="text"
           className="border border-gray-400 p-3 w-1/2 rounded-lg bg-white shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter New Skill"
            value={formData.newSkill}
            onChange={(e) => setFormData({ ...formData, newSkill: e.target.value })}
          />
          <button
            className="bg-blue-500 text-white p-2 rounded-lg"
            onClick={addNewSkill}
          >
            Add
          </button>
        </div>
      )}
    </div>


      <div className="flex gap-4">
      <input
          type="text"
           className="border border-gray-400 p-3 w-1/2 rounded-lg bg-white shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500"
           placeholder="Enter No. of Resources"
           value={formData.numberOfSMEs}
           onChange={(e) => {
         const value = e.target.value;
        if (/^\d*\.?\d*$/.test(value) || value === "") {
        setFormData({ ...formData, numberOfSMEs: value });
        }
     }}
    />

     {/* selecting the experience level */}
        <select
          className="border p-2 w-1/2"
          value={formData.experienceLevel}
          onChange={(e) => setFormData({ ...formData, experienceLevel: e.target.value })}
        >
          <option value="">Select Experience Level</option>
          <option value="Beginner">Beginner</option>
          <option value="Intermediate">Intermediate</option>
          <option value="senior">Senior</option>
        </select>
      </div>
{/* Project-Specific Requirements & Language Fluency */}
      <div className="flex gap-6">
        <div className="w-1/2">
          <h2 className="text-lg font-semibold">Project-Specific Requirements of Resources</h2>
          <TagInput placeholder="Add Relevant Certifications" formData={formData} setFormData={setFormData} field="certificateName" />
          <div className="mt-2">
            <label className="text-blue-500">
            Years of Relevant Domain Experience
            </label>
          <Select
            options={experienceOptions.map((exp) => ({ value: exp, label: exp }))}
            value={formData.domainExperienceRequired ? { value: formData.domainExperienceRequired, label: formData.domainExperienceRequired } : null}
            onChange={(selectedOption) => setFormData({ ...formData, domainExperienceRequired: selectedOption.value })}
          />
          </div>
        </div>

        <div className="w-1/2">
          <h2 className="text-lg font-semibold">Language Fluency</h2>
          <div className="mt-2">
          <Select
        isMulti
        options={languageOptions}
        value={languageOptions.filter((lang) => formData.languageFluency.includes(lang.value))}
        onChange={handleLanguageChange}
        placeholder="Select Languages"
      />
      </div>
          {formData.showNewLanguageInput && (
          <div className="flex gap-2 items-center mt-2">
          <input
            type="text"
            className="border border-gray-400 p-3 w-1/2 rounded-lg bg-white shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter New Language"
            value={formData.newLanguage}
            onChange={(e) => setFormData({ ...formData, newLanguage: e.target.value })}
          />
          <button className="bg-blue-500 text-white p-2 rounded-lg" onClick={addNewLanguage}>
            Add
          </button>
        </div>
    )}
        </div>
      </div>
      <ToastContainer />
      </form>
    </div>
  );
};

export default SMERequirements;


