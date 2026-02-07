import React, { useEffect, useState } from "react";
import "../style/TabView.css";
import PersonalInfo from "./PersonalInfo";
import OccupationExperience from "./OccupationExperience";
import ProjectAllocation from "./ProjectAllocation";
import NDAConfirmation from "./NDAConfirmation";
 
const TabView = ({ fileUplaodeCallBack, isNdaSigned }) => {
  const [activeTab, setActiveTab] = useState("personal");
  const [ndaConfirmed, setNdaConfirmed] = useState(isNdaSigned);
  const [showNdaPopup, setShowNdaPopup] = useState(false);
  const [isFileUploaded, setIsFileUploaded] = useState(false);
 
  useEffect(() => {
    setNdaConfirmed(isNdaSigned);
  }, [isNdaSigned]);
 
  const handleProjectTabClick = () => {
    if (!ndaConfirmed) {
      setShowNdaPopup(true); // Show NDA popup
    } else {
      setActiveTab("project"); // Allow tab switch if NDA is confirmed
    }
  };
 
  const handleNdaConfirmed = () => {
    setNdaConfirmed(true);
    setShowNdaPopup(false);
    setActiveTab("project"); // Automatically switch to Project Allocation
  };
 
  const renderTabContent = () => {
    if (activeTab === "personal") return <PersonalInfo setActiveTab={setActiveTab} />;
    if (activeTab === "occupation") return <OccupationExperience />;
    if (activeTab === "project") return <ProjectAllocation />;
  };
 
  useEffect(() => {
    console.log(isFileUploaded);
    fileUplaodeCallBack(isFileUploaded);
  }, [isFileUploaded]);
 
  return (
    <div className="tab-container">
      {/* Tab Headers */}
      <div className="tab-headers">
        <div
          className={`tab ${activeTab === "personal" ? "active" : ""}`}
          onClick={() => setActiveTab("personal")}
        >
          Personal Information
        </div>
        <div
          className={`tab ${activeTab === "occupation" ? "active" : ""}`}
          onClick={() => setActiveTab("occupation")}
        >
          Occupation and Experience
        </div>
        <div
          className={`tab ${activeTab === "project" ? "active" : ""}`}
          onClick={handleProjectTabClick}
        >
          Project Allocation
        </div>
      </div>
 
      {/* Tab Content */}
      <div className="tab-content">{renderTabContent()}</div>
 
      {/* NDA Confirmation Popup */}
      {showNdaPopup && (
        <NDAConfirmation
          isFileUploadeds={(e) => setIsFileUploaded(e)}
          onClose={() => setShowNdaPopup(false)}
          onConfirm={handleNdaConfirmed}
        />
      )}
    </div>
  );
};
 
export default TabView;