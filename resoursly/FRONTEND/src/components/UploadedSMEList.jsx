import React, { useState } from "react";
import { FiSearch, FiGrid, FiList } from "react-icons/fi";
import UserProfileCard from "./UserProfileCard";
import SMEFeature from "./SMEFeature";
import SearchBar from "./SearchBar";

const UploadedSMEList = ({ refresh }) => {
  const [view, setView] = useState("grid"); // Default to grid view
  const [searchQuery, setSearchQuery] = useState(""); // Search query state


  // Filter profiles based on search query
  // const filteredProfiles = profiles.filter((profile) => {
  //   const query = searchQuery.toLowerCase();
  //   return (
  //     profile.name?.toLowerCase().includes(query) ||
  //     profile.professionalExperience?.profession?.toLowerCase().includes(query) ||
  //     profile.professionalExperience?.primaryDisciplines?.some((skill) =>
  //       skill.toLowerCase().includes(query)
  //     ) ||
  //     profile.address?.toLowerCase().includes(query) ||
  //     profile.profileStatus?.availability?.toLowerCase().includes(query)
  //   );
  // });

  return (
    <div className="bg-custom-light sm:gap-0">
      {/* Title */}
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-gray-700 text-lg font-semibold">
        Pending Approval Resources
        </h2>

        {/* Search Input */}
        {/* {view!=="list" && (
       <SearchBar onChange={setSearchQuery} />
        
        )} */}
        {/* View Toggle Buttons */}
        <div className="flex items-center">
          <button
            className={`p-2 border rounded-md transition ${
              view === "list" ? "bg-blue-100 border-blue-600" : "bg-white border-gray-300"
            }`}
            onClick={() => setView("list")}
          >
            <FiList className={view === "list" ? "text-blue-600" : "text-gray-600"} />
          </button>
          <button
            className={`p-2 border rounded-md transition ${
              view === "grid" ? "bg-blue-100 border-blue-600" : "bg-white border-gray-300"
            }`}
            onClick={() => setView("grid")}
          >
            <FiGrid className={view === "grid" ? "text-blue-600" : "text-gray-600"} />
          </button>
        </div>
      </div>

      {/* Render Components Based on Active View */}
      {/* {filteredProfiles.length > 0 ? (
        view === "grid" ? (
          <UserProfileCard profiles={filteredProfiles} />
        ) : (
          
          <p className="text-gray-500"></p>
        )
      ) : (
        <div className="text-center text-gray-500 mt-4">
          No profiles found matching "{searchQuery}"
        </div>
      )}
      {view==="list" && <SMEFeature/>} */}
      {/* Render Components Based on Active View */}
      {view === "grid" ? <UserProfileCard refresh={refresh}/> : <p className="text-gray-500"></p>}
      {view === "list" && <SMEFeature />}
    </div>
  );
};

export default UploadedSMEList;
