import { FaRegFolder } from "react-icons/fa";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import BudgetFinancial from "./BudgetFinancial";
import SMERequirements from "./SMERequirements";
import ProjectTeam from "./ProjectTeam";
import DocumentUpload from "./DocumentUpload";


const ProjectDetails = ({ projectId, onSave }) => {

  const [formData, setFormData] = useState({
    projectId: projectId || "",
    projectName: "",
    projectStartDate: "",
    projectEndDate: "",
    ClientName: "",
    projectContactEmail: "",
    projectContactPhone: "",
    projectLocation: "",
    projectDescription: "",
  });

  // Update Project ID when fetched
  useEffect(() => {
    setFormData((prev) => ({ ...prev, projectId }));
  }, [projectId]);

  // Handle Input Change
  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prev) => ({ ...prev, [id]: value }));
  };

  // Convert Date Format (YYYY-MM-DD → DD-MM-YYYY)
  const convertToBackendFormat = (date) => {
    if (!date) return null;
    const [day, month, year] = date.split("-");
    return `${year}-${month}-${day}`;
  };


  // Disable past dates
  const today = new Date().toISOString().split("T")[0];

  
  const [showBudgetFinancial, setShowBudgetFinancial] = useState(false);
  const [showSMERequirements, setShowSMERequirements] = useState(false);
  const [showProjectTeam, setShowProjectTeam] = useState(false);
  const [showDocumentUpload, setShowDocumentUpload] = useState(false);

//validating all the fields
  const validate = () => {
    const errors = {};
    if (!formData.projectName) errors.projectName = "Project Name is required.";
    // if (!formData.projectStartDate) errors.projectStartDate = "Start Date is required.";
    // if (!formData.projectEndDate) errors.projectEndDate = "End Date is required.";
    // if (!formData.projectContactPerson) errors.projectContactPerson = "Contact Person is required.";
    if (!formData.projectContactEmail) {
      errors.projectContactEmail = "Email is required.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.projectContactEmail)) {
      errors.projectContactEmail = "Invalid Email Format.";
    }
    // if (!formData.projectContactPhone) {
    //   errors.projectContactPhone = "Phone Number is required.";
    // } else if (!/^\d{10}$/.test(formData.projectContactPhone)) {
    //   errors.projectContactPhone = "Phone Number must be 10 digits.";
    // }
    // if (!formData.projectLocation) errors.projectLocation = "Location is required.";
    // if (!formData.projectDescription) errors.projectDescription = "Description is required.";

    if (Object.keys(errors).length > 0) {
      Object.values(errors).forEach((err) => toast.error(err));
      return false;
    }
    return true;
  };

  // Submit Data
  const handleSubmit = async (e) => {
    e.preventDefault(); //use to prevent page reload


    if (!validate()) return;  //validating all the fields.

    // Convert dates before sending
  const formattedData = {
    ...formData,
    projectStartDate: convertToBackendFormat(formData.projectStartDate),
    projectEndDate: convertToBackendFormat(formData.projectEndDate),
  };
//we have used formattedData while posting data because in form data we are changing the dates format and then we are posting the data
   console.log("Submitting Data:", formattedData); // Debugging Log

    try {
      await axios.post("http://localhost:8040/projects", formattedData);
      toast.success("Project Created Successfully!");
      setShowBudgetFinancial(true);  // Show BudgetFinancial after successful save
      setShowSMERequirements(true);
      setShowProjectTeam(true);
      setShowDocumentUpload(true);
      onSave(); // use to save the form data and Move to the next step
    } catch (error) {
      console.error("Error creating project:", error.response?.data || error.message);
      toast.error("Failed to create project without having all the information.");
    }
  };
  


  return (
    <div>
      <h3 className="text-lg font-semibold text-blue-500 mb-4 flex items-center gap-2">
      <FaRegFolder />
        Project Details
      </h3>

      <form id="projectDetailsForm" onSubmit={handleSubmit} className="grid grid-cols-4 gap-6">
        <div>
          <label
            htmlFor="projectId"
            className="block text-sm font-medium text-gray-700"
          >
            Project ID*
          </label>
          <input
            id="projectId"
            value={formData.projectId}
            readOnly
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectName"
            className="block text-sm font-medium text-gray-700"
          >
            Project Name*
          </label>
          <input
            id="projectName"
            value={formData.projectName}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectStartDate"
            className="block text-sm font-medium text-gray-700"
          >
            Project Start Date*
          </label>
          <input
            id="projectStartDate"
            type="date"
            min={today}
            value={formData.projectStartDate}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectEndDate"
            className="block text-sm font-medium text-gray-700"
          >
            Project End Date*
          </label>
          <input
            id="projectEndDate"
            type="date"
            min={formData.projectStartDate || today}
            value={formData.projectEndDate}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectContactPerson"
            className="block text-sm font-medium text-gray-700"
          >
            Project Contact Person*
          </label>
          <input
            id="projectContactPerson"
            value={formData.projectContactPerson}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectContactEmail"
            className="block text-sm font-medium text-gray-700"
          >
            Project Contact Person Email*
          </label>
          <input
            id="projectContactEmail"
            value={formData.projectContactEmail}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectContactPhone"
            className="block text-sm font-medium text-gray-700"
          >
            Client's Name
          </label>
          <input
            id="projectContactPhone"
            value={formData.projectContactPhone}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          />
        </div>
        <div>
          <label
            htmlFor="projectLocation"
            className="block text-sm font-medium text-gray-700"
          >
            Project Location
          </label>
          <select
            id="projectLocation"
            value={formData.projectLocation}
            onChange={handleChange}
            className="mt-1 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition"
          >
            <option></option>
            <option>On-site</option>
            <option>Remote</option>
            <option>Hybrid</option>
          </select>
        </div>
        <div className="col-span-2">
          <label
            htmlFor="projectDescription"
            className="block text-sm font-medium text-gray-700"
          >
            Project Description
          </label>
          <textarea
            id="projectDescription"
            value={formData.projectDescription}
            onChange={handleChange}
            style={{ height: "100px", width: "800px" }}
            className="mt-1 h-40 border-2 border-black rounded-md shadow-sm hover:border-blue-500 focus:border-blue-500 focus:ring-0 resize-none transition"
          ></textarea>
        </div>
        <ToastContainer/>
      </form>
      {/* here we re sending project id prop to BudgetFinancial component */}
      {showBudgetFinancial && formData.projectId && <BudgetFinancial projectId={formData.projectId} />}
      {showSMERequirements && formData.projectId && <SMERequirements projectId={formData.projectId} />}
      {showProjectTeam && formData.projectId && <ProjectTeam projectId={formData.projectId} />}
      {showDocumentUpload && formData.projectId && <DocumentUpload projectId={formData.projectId} />}
    </div>
  );
};

export default ProjectDetails;