import React, { useEffect, useState } from "react";
import CreateProject from "./CreateProject";
import ManageProject from "./ManageProject";
import { useLocation } from "react-router-dom";

function Project() {
  const [activeTab, setActiveTab] = useState("projects"); // Set default to "projects"
  //   const [profiles, setProfiles] = useState([]);
  //   const [loading, setLoading] = useState(true);
  const { state } = useLocation();
  useEffect(() => {
    if (state?.name) {
      console.log("---111",state?.projectId,state?.name);
      setActiveTab("manage")
    }
  }, [state]);
  return (
    <>
      <div className="relative top-0 w-full h-[40px] bg-[#FAFBFC] flex items-center px-5">
        <div
          onClick={() => setActiveTab("projects")}
          className={`relative flex items-center cursor-pointer mr-6 ${
            activeTab === "projects" ? "text-blue-600" : "text-[#333333]"
          }`}
        >
          <h2 className="text-[11px] font-bold leading-[22px] tracking-normal uppercase">
            CREATE PROJECT
          </h2>
          {activeTab === "projects" && (
            <div className="absolute bottom-[-10px] left-0 right-0 mx-auto w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-blue-600"></div>
          )}
        </div>

        <div
          onClick={() => setActiveTab("manage")}
          className={`relative flex items-center cursor-pointer mr-6 ${
            activeTab === "manage" ? "text-blue-600" : "text-[#333333]"
          }`}
        >
          <h2 className="text-[11px] font-bold leading-[22px] tracking-normal uppercase">
            MANAGE PROJECTS
          </h2>
          {activeTab === "manage" && (
            <div className="absolute bottom-[-10px] left-0 right-0 mx-auto w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-blue-600"></div>
          )}
        </div>

        <div
          onClick={() => setActiveTab("sme")}
          className={`relative flex items-center cursor-pointer ${
            activeTab === "sme" ? "text-blue-600" : "text-[#333333]"
          }`}
        >
          {/* <h2 className="text-[11px] font-bold leading-[22px] tracking-normal uppercase">
            Resource ALLOCATION
          </h2>
          {activeTab === "sme" && (
            <div className="absolute bottom-[-10px] left-0 right-0 mx-auto w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[6px] border-t-blue-600"></div>
          )} */}
        </div>
      </div>

      <div className="bg-custom-light w-full h-full mt-2 p-4">
        {activeTab === "projects" && (
          <>
            <CreateProject setActiveTab={setActiveTab}/>
          </>
        )}
        {/* here we can add the components for manage project and sme allocation later */}
        {activeTab === "manage" && (
          <>
            <ManageProject recProjectId={state?.projectId ? state?.projectId : false} />
          </>
        )}

        {/* {activeTab === "sme" && (
          // Your SME Allocation content here//
        )} */}
      </div>
    </>
  );
}

export default Project;
