import React, { useEffect, useState } from "react";
import "../components/ProfileCard.css";
import UserImg from "../assets/dp.png";
import PersonalInfo from "./PersonalInfo.jsx";
import TabView from "./TabView";
import { useDispatch, useSelector } from "react-redux";
import {
  showProfile,
  hideProfile,
  viewOnlyProfileFalse,
} from "../feature/PopupSlice.js";
import profileClient from "../AxiosClient/ProfileClient.js";
import searchClient from "../AxiosClient/SearchClient.js";
import profileStatusClient from "../AxiosClient/profileStatusClient";
import { editProfileOff } from "../feature/EditMode.js";
import feedbackRatingClient from "../AxiosClient/FeedbackRatingClient.js";

const ProfileCard = () => {
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const dispatch = useDispatch();
  const [isNdaSigned, setIsNdaSigned] = useState(false);
  const [updateClicked, setUpdateClicked] = useState(false);
  const [status, setStatus] = useState("Available");
  const [dropdownVisible, setDropdownVisible] = useState(false);

  // const [name, setProfileName] = useState('');
  const [user, setUser] = useState({
    name: "Shubham",
    profession: "Senior Data Scientist",
    primaryEmail: "shubham@company.com",
    profileRating: "4",
  });

  console.log(user);

  {
    currentProfileId != 0 ? console.log(currentProfileId) : console.log("NA");
  }
  const isViewOnly = useSelector((state) => state.popup.isReadOnly);

  useEffect(() => {
    fetchProfileName();
    fetchFeedback();
    fetchNdaStatus(); // Initial fetch

    // it refreshes the fetchFeedback function in every 3 seconds
    const interval = setInterval(() => {
      fetchFeedback();
    }, 3000);

    // Cleanup function to clear interval when component unmounts
    return () => clearInterval(interval);
  }, []);

  const toggleNda = () => {
    setIsNdaSigned((prev) => !prev);
  };

  const fetchNdaStatus = async () => {
    try {
      const response = await profileStatusClient.get(`/${currentProfileId}`);
      setIsNdaSigned(response.data.ndaSigned); // Set state based on backend
      console.log("Fetched NDA status:", response.data.ndaSigned);
    } catch (error) {
      console.error("Error fetching NDA status:", error);
    }
  };

  const fetchFeedback = async () => {
    try {
      const resp = await feedbackRatingClient.get(`/${currentProfileId}`);

      console.log("Fetched feedbacks:", resp.data.feedbacks);

      const feedbacks = resp.data.feedbacks || [];
      const totalRatings = feedbacks.reduce(
        (sum, feedback) => sum + Number(feedback.ratings || 0),
        0
      );
      const avgRating =
        feedbacks.length > 0
          ? (totalRatings / feedbacks.length).toFixed(1)
          : ""; // Default rating is 3

      setUser((prevState) => ({
        ...prevState,
        profileRating: Math.round(avgRating), // Round off the rating
      }));
    } catch (error) {
      console.error("Error fetching feedback data:", error);
    }
  };

  const fetchProfileName = async () => {
    try {
      const res = await searchClient.get(`/${currentProfileId}`);

      // // Extract ratings from feedbacks array and calculate average
      // const feedbacks = res.data.feedbacks || [];
      // const totalRatings = feedbacks.reduce((sum, feedback) => sum + feedback.ratings, 0);
      // const avgRating = feedbacks.length > 0 ? totalRatings / feedbacks.length : 5; // Default rating is 3

      setUser((prevState) => ({
        ...prevState,
        name: res.data.name,
        primaryEmail: res.data.primaryEmail,
        // profileRating: Math.round(avgRating), // Round off the rating
        profession: res.data.professionalExperience?.profession || "",
      }));
    } catch (error) {
      console.error("Error fetching profile data:", error);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "Available":
        return "status-available";
      case "Inactive":
        return "status-inactive";
      case "Not Available":
        return "status-not-available";
      default:
        return "status-default";
    }
  };

  const toggleDropdown = () => {
    setDropdownVisible((prev) => !prev);
  };

  const selectStatus = (newStatus) => {
    setStatus(newStatus);
    setDropdownVisible(false);
  };

  const closeHandler = () => {
    dispatch(hideProfile());
    if (isViewOnly) {
      dispatch(viewOnlyProfileFalse());
    }
    dispatch(editProfileOff());
  };

  return (
    <div className="popup-backdrop">
      <div className="popup-container" onClick={(e) => e.stopPropagation()}>
        <div className="profile-card">
          {/* Profile Header */}
          <div className="profile-header">
            <button className="close-button" onClick={closeHandler}>
              &times;
            </button>
            <img src={UserImg} alt="Profile" className="profile-image" />
            <div className="profile-info">
              <h2 className="profile-name">
                {user.name}
                <div
                  className={`status-container ${
                    dropdownVisible ? "show-dropdown" : ""
                  }`}
                  onClick={toggleDropdown}
                >
                  <span
                    className={`status-dot ${getStatusClass(status)}`}
                    title={status}
                  ></span>
                  <span className="status-tooltip">{status}</span>
                  {/* Dropdown */}
                  <ul className="status-dropdown">
                    <li onClick={() => selectStatus("Available")}>Available</li>
                    <li onClick={() => selectStatus("Inactive")}>Inactive</li>
                    <li onClick={() => selectStatus("Not Available")}></li>
                  </ul>
                </div>
              </h2>
              <p className="profile-title">{user.profession}</p>
              <a href={`mailto:${user.primaryEmail}`} className="profile-email">
                {user.primaryEmail}
              </a>
              <div className="profile-rating">
                <span>{"⭐".repeat(user.profileRating)}</span>
              </div>
            </div>
          </div>

          {/* NDA Signed Toggle */}
          <div className="profile-nda">
            <label className="block text-gray-600 text-sm mb-1"></label>
            <div className="px-2 py-1 text-sm rounded-md">
              {isNdaSigned ? (
                <button className="inline-flex items-center px-4 py-2 rounded-full bg-green-100 text-green-700">
                  <div className="w-5 h-5 flex items-center justify-center bg-green-500 rounded-full text-white text-sm font-bold mr-2">
                    ✔
                  </div>
                  <span className="font-semibold">NDA Signed</span>
                </button>
              ) : (
                <button className="inline-flex items-center px-4 py-2 rounded-full bg-[#FEF6F1] text-[#EC7231] text-sm font-medium">
                  <div className="w-5 h-5 flex items-center justify-center bg-[#EC7231] rounded-full text-white text-sm font-bold mr-2">
                    !
                  </div>
                  <span className="font-semibold">NDA Pending</span>
                </button>
              )}
            </div>
          </div>

          <TabView
            isNdaSigned={isNdaSigned}
            fileUplaodeCallBack={(e) => setIsNdaSigned(e)}
          />
        </div>
      </div>
    </div>
  );
};

export default ProfileCard;
