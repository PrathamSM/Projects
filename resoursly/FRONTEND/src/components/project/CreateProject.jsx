// CreateProject.js

import React, { useState, useEffect } from "react";
import ProjectDetails from "./ProjectDetails";
import axios from "axios";
import { ToastContainer } from "react-toastify";
import BudgetFinancial from "./BudgetFinancial";
import SMERequirements from "./SMERequirements";
import ProjectTeam from "./ProjectTeam";
import DocumentUpload from "./DocumentUpload";

const CreateProject = ({ setActiveTab }) => {
  const [isCreatingProject, setIsCreatingProject] = useState(false); // closes the project detail panel and set it to the create new project when we again click the create project
  const [currentStep, setCurrentStep] = useState(1);
  const [projectId, setProjectId] = useState("");

  const steps = [
    "Project details",
    "Budget and Financial",
    "Resource Requirements",
    "Document upload (if any)",
  ];


  useEffect(() => {
    if (isCreatingProject) {
      fetchNextProjectId();
    }
  }, [isCreatingProject]);

  const handleNext = () => {
    if (currentStep < steps.length) setCurrentStep(currentStep + 1);
  };

  const handlePrevious = () => {
    if (currentStep > 1) setCurrentStep(currentStep - 1);
  };

  // Fetch Project ID when user clicks "Create Project"
const fetchNextProjectId = async () => {
  try {
    const response = await axios.get("http://localhost:8040/projects/next-id");
    console.log("Project ID:", response.data);
    setProjectId(response.data); // Directly set project ID
  } catch (error) {
    console.error("Error fetching project ID:", error);
  }
};

const handleNextStep = () => {
  if (currentStep === 4) {
    document.getElementById("DocumentUploadForm")?.dispatchEvent(new Event("submit", { cancelable: true }));
    
    setTimeout(() => {
      setActiveTab("manage"); // Redirect to Manage Projects tab.
    }, 500);
  } else {
    if (currentStep === 1) {
      document.getElementById("projectDetailsForm").requestSubmit();
    } else if (currentStep === 2) {
      document.getElementById("budgetFinancialForm").requestSubmit();
    } else if (currentStep === 3) {
      document.getElementById("SMERequirementForm").requestSubmit();
    }
  }
};


  return (
    
    <div className="bg-custom-light flex flex-col gap-4 sm:gap-6">
      {!isCreatingProject ? (
        <>
          <div
            className="border border-gray-300 rounded-md flex flex-col items-center justify-center p-6 bg-white shadow-md max-w-lg mx-auto"
            style={{ height: "300px", width: "1000px" }}
          >
            <button
              onClick={() => {
                setIsCreatingProject(true);
                fetchNextProjectId();
              }}
              className="bg-blue-500 text-white px-8 py-5 rounded-md hover:bg-blue-700 transition"
            >
              Create Project
            </button>
            <div className="mt-6 text-left">
    <h1 className="text-lg font-semibold text-gray-700 text-center">Create a new project</h1>
    <p className="text-sm text-gray-500 mt-2">
      Create and define your projects.
    </p>
  </div>
          </div>
        </>
      ) : (
        <>
          {/* Step-Based Form */}
          <div className="bg-white rounded-md shadow-md p-20 w-full max-w-7xl mx-auto">
            {/* Step Navigation */}
            <div className="flex justify-between mb-8">
              {steps.map((step, index) => (
                <div
                  key={index}
                  className={`flex flex-col items-center ${
                    currentStep === index + 1
                      ? "text-blue-600"
                      : "text-gray-400"
                  }`}
                >
                  {/* Blue Circle with Step Number */}
                  <div
                    className={`flex items-center justify-center w-10 h-10 rounded-full mb-2 ${
                      currentStep === index + 1
                        ? "bg-blue-600 text-white"
                        : "bg-gray-300 text-gray-700"
                    }`}
                  >
                    {index + 1}
                  </div>
                  {/* Step Title */}
                  <span
                    className={`text-sm font-semibold text-center ${
                      currentStep === index + 1 ? "border-b-2 border-blue-600" : ""
                    }`}
                  >
                    {step}
                  </span>
                </div>
              ))}
            </div>

            {/* Step Content */}
            <div>
              {currentStep === 1 && <ProjectDetails projectId={projectId} onSave={handleNext}/>} 
              {currentStep === 2 && projectId && <BudgetFinancial projectId={projectId} onSave={handleNext} />}
              {currentStep === 3 && projectId && <SMERequirements projectId={projectId} onSave={handleNext} />}
              {currentStep === 4 && projectId && <DocumentUpload projectId={projectId} onSave={handleNext} />}
              {/*here i can add other steps components*/}
            </div>

            {/* Navigation Buttons */}
            <div className="flex justify-between items-center w-full">
            {currentStep > 1 && (
                       <button
                        onClick={handlePrevious}
                        className="px-4 py-2 rounded-md bg-blue-500 text-white hover:bg-blue-700"
                       >
               {steps[currentStep - 2]} {/* Show previous step name */}
                  </button>
           )}
              <button onClick={handleNextStep} className={`px-4 py-2 rounded-md ${
                  currentStep === steps.length+1
                    ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                    : "bg-blue-500 text-white hover:bg-blue-700"
                }`}
                style={{ marginLeft: "auto" }}>
      {currentStep === 4 ? "Save & Finish" : "Save & Next"}
    </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default CreateProject;
