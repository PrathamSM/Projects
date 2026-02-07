import React, { useEffect, useState } from "react";
import {
  FaStar,
  FaEnvelope,
  FaEllipsisV,
  FaEye,
  FaTrash,
} from "react-icons/fa";
import { FaSpinner } from "react-icons/fa";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { showProfile, viewOnlyProfile } from "../feature/PopupSlice";
import { setProfileId } from "../feature/CurrentProfile";
import searchClient from "../AxiosClient/SearchClient";
import feedbackRatingClient from "../AxiosClient/FeedbackRatingClient";
import SearchBar from "./SearchBar";
import ProfileDetails from "../AxiosClient/ProfileDetails";
import axios from "axios";

const UserProfileCard = ({ refresh }) => {
  const [currentPage, setCurrentPage] = useState(1); // Track the current page
  const [selectedProfile, setSelectedProfile] = useState(null); // State for selected profile
  const [showMenu, setShowMenu] = useState(false);
  const [activeMenu, setActiveMenu] = useState(null);
  const [isHoveredLeft, setIsHoveredLeft] = useState(false);
  const [isHoveredRight, setIsHoveredRight] = useState(false);
  const cardsPerPage = 8; // Number of cards to display at a time
  const [profiles, setProfiles] = useState([]);
  const [ratings, setRatings] = useState({});
  const [activeProfileId, setActiveProfileId] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  var [loading, setLoading] = useState(true);

  // useEffect(() => {
  //   let isFirstFetch = true; // Track whether it's the first fetch
  
  //   const fetchProfiles = async () => {
  //     if (isFirstFetch) setLoading(true); // Show spinner only on the first fetch
  
  //     try {
  //       const response = await searchClient.get();
  //       const data = response.data;
  //       const unapprovedProfiles = data.filter(
  //         (profile) => profile.profileStatus?.isApproved === false
  //       );
  
  //       setProfiles(unapprovedProfiles);
  //     } catch (error) {
  //       console.error("Error fetching profiles:", error);
  //     } finally {
  //       if (isFirstFetch) {
  //         setLoading(false); // Hide spinner after the first fetch
  //         isFirstFetch = false; // Prevent re-showing the spinner on subsequent fetches
  //       }
  //     }
  //   };


  useEffect(() => {
    let isFirstFetch = true; // Track whether it's the first fetch
 
    const fetchProfiles = async () => {
      if (isFirstFetch) setLoading(true); // Show spinner only on the first fetch
 
      try {
        const response = await axios.get("http://localhost:8090/profiles/unapproved");
        const data = response.data;
        const unapprovedProfiles = data.filter(
          (profile) => profile.profileStatus?.isApproved === false
        );
 
        setProfiles(data);
       
  // Fetch ratings for each profile
  const ratingsData = {};
  for (const profile of unapprovedProfiles) {
    const feedbackRes = await feedbackRatingClient.get(`/${profile.id}`);  ///added by us
    if (feedbackRes.data && feedbackRes.data.feedbacks) {
      ratingsData[profile.id] = feedbackRes.data.feedbacks;
    }
  }
  setRatings(ratingsData);

 
 
 
      } catch (error) {
        console.error("Error fetching profiles:", error);
      } finally {
        if (isFirstFetch) {
          setLoading(false); // Hide spinner after the first fetch
          isFirstFetch = false; // Prevent re-showing the spinner on subsequent fetches
        }
      }
    };
  
    fetchProfiles(); // Initial fetch
  
    const interval = setInterval(fetchProfiles, 3000); // Set interval for updates
  
    return () => clearInterval(interval); // Cleanup interval on unmount
  }, [refresh]);
  
 // Render the rating stars
   const renderRating = (profileId) => {
    const profileRatings = ratings[profileId];
    if (!profileRatings || profileRatings.length === 0) return null;
 
    const averageRating =
      profileRatings.reduce((sum, feedback) => sum + feedback.ratings, 0) /
      profileRatings.length;
 
    return (
      <div className="flex items-center mt-1 text-yellow-500">
        {[...Array(Math.round(averageRating))].map((_, i) => (
          <FaStar key={i} />
        ))}
      </div>
    );
  };
  // Filter profiles based on searchQuery
  const filteredProfiles = profiles.filter((profile) => {
    const query = (searchQuery || "").toLowerCase(); // Ensure searchQuery is a string
    return (
      (profile.name && profile.name.toLowerCase().includes(query)) ||
      (profile.professionalExperience?.profession &&
        profile.professionalExperience.profession
          .toLowerCase()
          .includes(query)) ||
      (Array.isArray(profile.professionalExperience?.primaryDisciplines) &&
        profile.professionalExperience.primaryDisciplines.some((skill) =>
          skill.toLowerCase().includes(query)
        )) ||
      (profile.address && profile.address.toLowerCase().includes(query)) ||
      (profile.profileStatus?.availability &&
        profile.profileStatus.availability.toLowerCase().includes(query))
    );
  });

  const dispatch = useDispatch();

  const handleCardClick = (profileId) => {
    setActiveProfileId(profileId);
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    setActiveMenu(null);
  };

  const viewClickHandler = (profileId) => {
    console.log(`---VIEW CLICKED--- for ID : ${profileId}`);
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    dispatch(viewOnlyProfile());
  };

  const handleMenuClose = () => {
    setActiveMenu(null);
  };

  // Calculate the start and end indices for the current page
  const indexOfLastProfile = currentPage * cardsPerPage;
  const indexOfFirstProfile = indexOfLastProfile - cardsPerPage;

  // Get the profiles for the current page
  const visibleProfiles = filteredProfiles.slice(
    indexOfFirstProfile,
    indexOfLastProfile
  );

  // Calculate the total number of pages
  const totalPages = Math.ceil(filteredProfiles.length / cardsPerPage);

  const getStatusColor = (status) => {
    if (status === "Available") return "bg-green-500";
    if (status === "Inactive") return "bg-yellow-500";
    if (status === "Not Available") return "bg-red-500";
    return "bg-green-500";
  };

  const handlePrevious = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const getRandomColor = () => {
    const colors = [
      "bg-sky-300",
      "bg-gray-500",
      "bg-pink-500",
      "bg-yellow-300",
      "bg-purple-300",
    ];
    return colors[Math.floor(Math.random() * colors.length)];
  };

  const getInitials = (name) => {
    if (!name) return "N/A";
    const words = name.split(" ");
    return words.length > 1
      ? words[0][0].toUpperCase() + words[1][0].toUpperCase()
      : words[0][0].toUpperCase();
  };

  return (
    <div className="relative p-2">
      {/* search bar */}
      <SearchBar onChange={setSearchQuery} />

      {/* Left Navigation Button */}
      <div
        className="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 group"
        onMouseEnter={() => setIsHoveredLeft(true)}
        onMouseLeave={() => setIsHoveredLeft(false)}
      >
        <button
          onClick={handlePrevious}
          className={`p-2 rounded-full bg-white shadow-md transition-opacity duration-300 ${
            isHoveredLeft && currentPage > 1
              ? "opacity-100 text-blue-500"
              : "opacity-0 text-gray-300"
          }`}
          disabled={currentPage === 1}
        >
          <FaChevronLeft size={24} />
        </button>
      </div>

      {/* Right Navigation Button */}
      <div
        className="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 group"
        onMouseEnter={() => setIsHoveredRight(true)}
        onMouseLeave={() => setIsHoveredRight(false)}
      >
        <button
          onClick={handleNext}
          className={`p-2 rounded-full bg-white shadow-md transition-opacity duration-300 ${
            isHoveredRight && currentPage < totalPages
              ? "opacity-100 text-blue-500"
              : "opacity-0 text-gray-300"
          }`}
          disabled={currentPage === totalPages}
        >
          <FaChevronRight size={24} />
        </button>
      </div>

      {loading && (
        <div className="flex justify-center items-center py-10">
          <FaSpinner className="animate-spin text-blue-500 text-3xl" />
        </div>
      )}

      {/* Show message if no profiles are available */}
      {!loading && profiles.length === 0 && (
        <div className="text-center text-gray-500 mt-4">
          No approval is pending
        </div>
      )}

      {/* Cards Container */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {!loading &&
          profiles.length > 0 &&
          visibleProfiles.map((profile) => (
            <div
              key={profile.id}
              className="border rounded-lg shadow-md bg-white hover:shadow-lg transition-transform transform hover:-translate-y-2"
              onClick={() => handleCardClick(profile.id)}
            >
              
              {/* Header */}
              <div className="flex justify-between items-center p-4 relative">
                {/* Badge */}
              <div className="absolute top-5 right-10 bg-red-100 text-red-500 text-sm px-2 py-1 rounded">
                    Approval Pending
        
                  </div>
                
                <div className="flex items-center">
                  <div
                    className={`w-10 h-10 flex items-center justify-center rounded-full text-white font-bold ${getRandomColor()}`}
                  >
                    {getInitials(profile.name || "Unknown User")}
                  </div>
                  
                  <div
                    className={`w-3 h-3 rounded-full absolute bottom-3 left-12 ${getStatusColor(
                      profile.profileStatus?.availability || "Available"
                    )}`}
                    title={profile.profileStatus?.availability || "Available"}
                  ></div>
                </div>
                {/* Dropdown Menu */}
                <div className="relative">
                  <button
                    className="text-gray-500 focus:outline-none"
                    onClick={(e) => {
                      e.stopPropagation(); // Prevent the card click from triggering
                      setActiveMenu((prev) =>
                        prev === profile.id ? null : profile.id
                      );
                    }}
                  >
                    <FaEllipsisV className="text-sky-500 cursor-pointer hover:bg-sky-200" /> 
                  </button>
                  {activeMenu === profile.id && (
                    <div
                      className="absolute right-0 mt-2 bg-white border rounded-md shadow-md w-32 z-10"
                      onMouseEnter={() => setActiveMenu(profile.id)} // Keep menu open when hovered
                      onMouseLeave={handleMenuClose} // Close the menu when the mouse leaves
                    >
                      <button
                        className="flex items-center w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-sky-100 hover:text-blue-700 font-bold"
                        onClick={() => viewClickHandler(profile.id)}
                      >
                        <FaEye className="mr-2 text-blue-500" />
                        View
                      </button>
                      <button
                        className="flex items-center w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-sky-100 hover:text-blue-700 font-bold"
                        onClick={() => console.log("Delete Clicked")}
                        disabled
                      >
                        <FaTrash className="mr-2 text-red-500" />
                        Delete
                      </button>
                    </div>
                  )}
                </div>
              </div>

             

              {/* Details */}
              <div className="p-4">
                <h3 className="text-lg font-bold flex items-center gap-2">
                  {profile.name || "Unknown User"}
                  <a
                    href={`mailto:${profile.email || ""}`}
                    title={`Email ${profile.name || "User"}`}
                    onClick={(e) => e.stopPropagation()}
                  >
                    <FaEnvelope className="text-blue-500 cursor-pointer hover:text-blue-700" />
                  </a>
                </h3>
                {/* <p className="text-blue-500 text-sm">
                  {profile.professionalExperience?.profession ||
                    "Unknown Profession"}
                </p> */}

                {/* Rating */}
                {/* <div className="flex items-right mt-1 text-yellow-500">
                  {[...Array(profile.feedbackRating?.rating || 0)].map(
                    (_, i) => (
                      <FaStar key={i} />
                    )
                  )}
                </div> */}
                {/* Display Rating */}
              {renderRating(profile.id)}
                <hr className="my-4" />

                {/* Additional Info */}
                <div className="text-sm">
                  <p>
                    <b>Skills:</b>{" "}
                    <span className="text-gray-600">
                      {profile.professionalExperience?.primaryDisciplines?.join(
                        ", "
                      ) || "N/A"}
                    </span>
                  </p>
                  <p>
                    <b>Location:</b>{" "}
                    <span className="text-gray-600">
                      {profile.address || "Unknown"}
                    </span>
                  </p>
                </div>
              </div>
            </div>
          ))}
      </div>

      {/* Pagination Controls */}
      <div className="flex justify-center items-center space-x-2 mt-4">
        {!loading && profiles.length > 0 && (
          <>
            <button
              onClick={handlePrevious}
              disabled={currentPage === 1}
              className="p-2 bg-gray-300 rounded-full"
            >
              <FaChevronLeft />
            </button>
            <span className="text-sm">
              Page {currentPage} of {totalPages}
            </span>
            <button
              onClick={handleNext}
              disabled={currentPage === totalPages}
              className="p-2 bg-gray-300 rounded-full"
            >
              <FaChevronRight />
            </button>
          </>
        )}
      </div>

      {!loading && filteredProfiles.length === 0 && (
        <div className="text-center text-gray-500 mt-4">
          No profiles found matching "{searchQuery}"
        </div>
      )}
    </div>
  );
};

export default UserProfileCard;
