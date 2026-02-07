import React, { useEffect, useState } from "react";
import FileDropZone from "./FileDropZone";
import UploadedSMEList from "./UploadedSMEList";
import SMEList from "./SMEList";
import axios from "axios";
import { useSelector } from "react-redux";
import ProfileCard from "./ProfileCard";
import { useLocation } from "react-router-dom";

function Upload() {
  const [activeTab, setActiveTab] = useState("upload");
  const [profiles, setProfiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const { state } = useLocation();
  const handleUploadSuccess = () => {
    setRefresh((prev) => !prev); // Toggle state to refresh profiles
    setShowPopup(true); // Show success popup

    // Hide popup after 3 seconds
    setTimeout(() => {
      setShowPopup(false);
    }, 3000);
  };
 
  useEffect(() => {
    if (state) {
      console.log("---111");
      setActiveTab("sme")
    }
  }, [state]);
  return (
    <>
      <div className="relative top-0 w-full h-[40px] bg-[#FAFBFC] flex items-center px-5">
        <div
          onClick={() => setActiveTab("upload")}
          className={`relative flex items-center cursor-pointer mr-6 ${
            activeTab === "upload" ? "text-blue-600" : "text-[#333333]"
          }`}
        >
          <h2 className="text-[11px] font-bold leading-[22px] tracking-normal uppercase">
            UPLOAD PROFILE
          </h2>
          {activeTab === "upload" && (
            <div className="absolute bottom-[-10px] left-0 right-0 mx-auto w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-blue-600"></div>
          )}
        </div>

        <div
          onClick={() => setActiveTab("sme")}
          className={`relative flex items-center cursor-pointer ${
            activeTab === "sme" ? "text-blue-600" : "text-[#333333]"
          }`}
        >
          <h2 className="text-[11px] font-bold leading-[22px] tracking-normal uppercase">
            APPROVED LIST
          </h2>
          {activeTab === "sme" && (
            <div className="absolute bottom-[-10px] left-0 right-0 mx-auto w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-blue-600"></div>
          )}
        </div>
      </div>

      <div className="bg-custom-light w-full h-full mt-2 p-4">
        {activeTab === "upload" && (
          <>
            <FileDropZone onUploadSuccess={handleUploadSuccess} />
            {showPopup && (
              <div className="absolute top-10 left-1/2 transform -translate-x-1/2 bg-green-500 text-white py-2 px-4 rounded shadow-lg">
                Upload Successful!
              </div>
            )}
            <UploadedSMEList refresh={refresh} />
          </>
        )}
        {activeTab === "sme" && <SMEList />}
      </div>
    </>
  );
}

export default Upload;
