import React, { useEffect, useState } from "react";
import "../style/PersonalInfo.css";
import { FaCalendarAlt, FaPenSquare } from "react-icons/fa";
import { Country, State, City } from "country-state-city";
import ReactDatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // Import Toastify styles
import axios from "axios";
import { useSelector } from "react-redux";
import profileClient from "../AxiosClient/ProfileClient";
import profileStatusClient from "../AxiosClient/profileStatusClient";
import ProfileDetails from "../AxiosClient/ProfileDetails";

const PersonalInfo = ({setActiveTab}) => {
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const isViewOnly = useSelector((state) => state.popup.isReadOnly);
  const isEditOnly = useSelector((state) => state.editProfile.isEdit);

  const [profile, setProfile] = useState({});
  const [isApproved, setIsApproved] = useState(false);

  //21/01 kg
  const handleApprove = async () => {
    if (!formData.primaryEmail || !formData.contactNo) {
      // Show a warning toast if the required fields are missing
      toast.error(
        "Primary email and contact number are required to approve the profile."
      );
      return;
    }
    try {
      // Fetch profile data using the GET API
      const profileResponse = await ProfileDetails.get(`/${currentProfileId}`);
      if (profileResponse.status !== 200 || !profileResponse.data) {
        const profileData = profileResponse.data;
        // const approvedStatus = profileData.profileStatus.isApproved;
        setFormData(profileData);
        setIsApproved(profileData.profileStatus.isApproved);
        console.log("Profile fetched:", profileData);
        toast.error("Failed to fetch profile details.");
        return;
      }

      const profileData = profileResponse.data;
      console.log("Profile Data:", profileData);

      console.log("isApproved:", profileData.profileStatus.isApproved);

      // Validate required fields
      if (
        !profileData.primaryEmail ||
        !profileData.contactNo ||
        !profileData.professionalExperience.profession ||
        !profileData.professionalExperience.qualification ||
        !profileData.professionalExperience.experienceYears ||
        !profileData.professionalExperience.relevantExperience ||
        !profileData.professionalExperience.primaryDisciplines ||
        !profileData.professionalExperience.secondaryDisciplines
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

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    const res = await profileClient.get(`/${currentProfileId}`);
    const { id, profileLifecycle, ...formDataWithoutProfileLifecycle } =
      res.data;
    console.log(formDataWithoutProfileLifecycle);
    formDataWithoutProfileLifecycle.contactNo =
      formDataWithoutProfileLifecycle.contactNo.replace(/[\+\s]/g, "");
    setFormData(formDataWithoutProfileLifecycle);
  };

  //09/01
  const updateProfile = async () => {
    console.log("-----Executing Update Profile Method");

    const res = await profileClient
      .put(`/${currentProfileId}`, formData)
      .then((response) => response)

      .catch((error) => {
        console.log("There is some error while updating profile");
      });

    console.log("Response after updation");
    console.log(res);
    console.log("Update profile method executed!!");
  };

  const [formData, setFormData] = useState({
    dateOfBirth: "12-Dec-2022",
    primaryEmail: "shubham@company.com",
    secondaryEmail: "shubham.a@gmail.com",
    address: "123 Market Street, Suite 500",
    city: "",
    state: "",
    country: "",
    pinCode: "94105",
    contactNo: "91234567890",
  });

  const [formErrors, setFormErrors] = useState({
    dateOfBirth: "",
    primaryEmail: "",
    secondaryEmail: "",
    address: "",
    city: "",
    state: "",
    country: "",
    pinCode: "",
    contactNo: "",
  });

  //09/01 Kg (for cities and states dropdown )
  const [states, setStates] = useState([]);
  const [cities, setCities] = useState([]);

  // Fetching states on change of country and on change of states fetching cities
  useEffect(() => {
    if (formData.country) {
      const countryCode = Country.getAllCountries().find(
        (c) => c.name === formData.country
      )?.isoCode;
      if (countryCode) {
        const statesList = State.getStatesOfCountry(countryCode);
        setStates(statesList);
        console.log("Fetched States:", statesList);
      }
    }
    if (formData.state) {
      const countryCode = Country.getAllCountries().find(
        (c) => c.name === formData.country
      )?.isoCode;
      const stateCode = State.getStatesOfCountry(countryCode).find(
        (s) => s.name === formData.state
      )?.isoCode;
      if (countryCode && stateCode) {
        const citiesList = City.getCitiesOfState(countryCode, stateCode);
        setCities(citiesList);
        console.log("Fetched Cities:", citiesList);
      }
    }
  }, [formData.country, formData.state]);

  const validate = (name, value) => {
    let error = "";
    switch (name) {
      case "dateOfBirth":
        if (!value) {
          error = "Date of birth is required.";
        } else {
          const today = new Date();
          const birthDate = new Date(value);

          if (birthDate > today) {
            error = "Date of birth cannot be in the future.";
          }

          const maxAge = 100;
          const age = today.getFullYear() - birthDate.getFullYear();
          if (age > maxAge) {
            error = `Age cannot exceed ${maxAge} years.`;
          }
        }
        break;
      case "primaryEmail":
        if (!value) {
          error = "Primary email is required.";
        } else if (!/\S+@\S+\.\S+/.test(value)) {
          error = "Please enter a valid email address.";
        }
        break;
      case "secondaryEmail":
        if (value && !/\S+@\S+\.\S+/.test(value)) {
          error = "Please enter a valid email address.";
        }
        break;
      case "address":
        if (!value) {
          error = "Address is required.";
        } else if (!/^[a-zA-Z0-9\s,./]+$/.test(value)) {
          error =
            "Address can only contain alphabets, numbers, commas, full stops, and slashes";
        }
        break;
      case "city":
        if (!value) {
          error = "City is required.";
        }
        break;
      case "state":
        if (!value) {
          error = "State is required.";
        }
        break;
      case "country":
        if (!value) {
          error = "Country is required.";
        }
        break;
      case "pinCode":
        if (!value) {
          error = "PIN code is required.";
        }
        // else if (!/^\d{5}$/.test(value)) {
        //   error = "Please enter a valid PIN code.";
        // }
        break;
      case "contactNo":
        value = value.replace(/[^0-9]/g, ""); //To allow only numbers
        if (value.length > 15) {
          value = value.slice(0, 15);
        }
        error = !value
          ? "Contact Number is required!"
          : value.length < 5
          ? "Contact No must be at least 5 digits"
          : "";
        break;
      default:
        break;
    }
    return error;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    const error = validate(name, value);

    if (name === "country") {
      setFormData({ ...formData, country: value, state: "", city: "" });
      setStates([]);
      setCities([]);
    } else if (name === "state") {
      setFormData({ ...formData, state: value, city: "" });
      setCities([]);
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleDateChange = (date) => {
    // setFormData({ ...formData, dateOfBirth: date });

    //19/12
    const error = validate("dateOfBirth", date); // Validate the new date
    setFormData({ ...formData, dateOfBirth: date }); // Update the form data
    setFormErrors({ ...formErrors, dateOfBirth: error }); // Update errors
  };

  //21/12
  const handlePinCodeChange = (event) => {
    let { value } = event.target;

    value = value.replace(/[^0-9]/g, ""); //To allow only numbers

    if (value.length > 6) {
      value = value.slice(0, 6); //Limit to 6 digits
    }

    setFormData({ ...formData, pinCode: value });

    const error = value.length < 6 ? "PIN Code must be of 6 digits" : "";

    setFormErrors({ ...formErrors, pinCode: error });
  };

  //21/12
  const handleContactChange = (event) => {
    let { value } = event.target;
    value = value.replace(/[^0-9]/g, ""); //To allow only numbers
    if (value.length > 10) {
      value = value.slice(0, 10);
    }

    setFormData({ ...formData, contactNo: value });

    const error = value.length < 10 ? "Contact No must be of 10 digits" : "";

    setFormErrors({ ...formErrors, contactNo: error });
  };

  const handleSubmit = (event) => {
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
          progress: undefined,
        });
      }
    });

    setFormErrors(errors);

    if (!hasErrors) {
      console.log("Sending data to backend");
      console.log(formData);
      updateProfile(formData);

      toast.success("Form updated successfully!", {
        
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      setTimeout(() => {
        setActiveTab('occupation');
      }, 1000);
    } else {
      console.log("Form has errors. Please fix them before submitting.");
    }
  };

  return (
    <form className="personal-info-form" onSubmit={handleSubmit}>
      <h2 className="form-title">Personal Information</h2>

      <div className="form-grid">
        {/* Date of Birth */}
        <div className="form-group">
          <ReactDatePicker
            selected={formData.dateOfBirth}
            onChange={handleDateChange}
            disabled={isViewOnly}
            className={formData.dateOfBirth ? "has-value error" : ""}
            dateFormat="dd-MMMM-yyyy"
          />

          {/* <label className={formData.dateOfBirth ? "has-value" : ""}>Date of birth</label> */}
          <label
            id="date-label"
            className={formErrors.dateOfBirth ? "error" : ""}
          >
            Date of Birth
          </label>
          {!isViewOnly && <FaCalendarAlt className="icon" />}
        </div>

        {/* Primary Email */}
        <div className="form-group">
          <input
            type="email"
            name="primaryEmail"
            value={formData.primaryEmail}
            onChange={handleChange}
            disabled={isViewOnly}
            // className={formErrors.primaryEmail ? "has-value error" : "has-value"}
            className={`${formData.primaryEmail ? "has-value" : ""} ${
              formErrors.primaryEmail ? "error" : ""
            }`}
          />
          <label className={formErrors.primaryEmail ? "error" : ""}>
            Primary email*
          </label>
          {!isViewOnly && <FaPenSquare className={"icon"} />}
        </div>

        {/* Secondary Email */}
        <div className="form-group">
          <input
            type="email"
            name="secondaryEmail"
            value={formData.secondaryEmail}
            onChange={handleChange}
            disabled={isViewOnly}
            // className={formErrors.secondaryEmail ? "has-value error" : "has-value"}
            className={`${formData.secondaryEmail ? "has-value" : ""} ${
              formErrors.secondaryEmail ? "error" : ""
            }`}
          />
          <label className={formErrors.secondaryEmail ? "error" : ""}>
            Secondary email
          </label>
          {!isViewOnly && <FaPenSquare className={"icon"} />}
        </div>

        {/* Address */}
        <div className="form-group">
          <input
            type="text"
            name="address"
            value={formData.address}
            disabled={isViewOnly}
            onChange={handleChange}
            // className={formErrors.address ? "has-value error" : "has-value"}
            className={`${formData.address ? "has-value" : ""} ${
              formErrors.address ? "error" : ""
            }`}
          />
          <label className={formErrors.address ? "error" : ""}>Address</label>
        </div>

        {/* Country Dropdown */}
        <div className="form-group">
          <select
            name="country"
            value={formData.country != null ? formData.country : ""}
            onChange={handleChange}
            disabled={isViewOnly}
            className={`${formData.country ? "has-value" : ""} ${
              formErrors.country ? "error" : ""
            }`}
          >
            <option value="" disabled></option>
            {Country.getAllCountries().map((country) => (
              <option key={country.isoCode} value={country.name}>
                {country.name}
              </option>
            ))}
          </select>
          <label className={formErrors.country ? "error" : ""}>Country</label>
        </div>
        {/* State Dropdown */}
        <div className="form-group">
          <select
            name="state"
            value={formData.state != null ? formData.state : ""}
            disabled={isViewOnly}
            onChange={handleChange}
            // className={formErrors.state ? "has-value error" : "has-value"}
            className={`${formData.state ? "has-value" : ""} ${
              formErrors.state ? "error" : ""
            }`}
          >
            <option value="" disabled></option>
            {states.map((state) => (
              <option key={state.isoCode} value={state.name}>
                {state.name}
              </option>
            ))}
          </select>
          <label className={formErrors.state ? "error" : ""}>State</label>
        </div>

        {/* City Dropdown */}
        <div className="form-group">
          <select
            name="city"
            value={formData.city}
            onChange={handleChange}
            disabled={isViewOnly}
            // className={formErrors.city ? "has-value error" : "has-value"}
            className={`${formData.city ? "has-value" : ""} ${
              formErrors.city ? "error" : ""
            }`}
          >
            <option value="" disabled></option>
            {cities.map((city) => (
              <option key={city.name} value={city.name}>
                {city.name}
              </option>
            ))}
          </select>
          <label className={formErrors.city ? "error" : ""}>City</label>
        </div>
        {/* PIN Code */}
        <div className="form-group">
          <input
            type="text"
            name="pinCode"
            value={formData.pinCode}
            disabled={isViewOnly}
            onChange={handlePinCodeChange}
            // className={formErrors.pinCode ? "has-value error" : "has-value"}
            className={`${formData.pinCode ? "has-value" : ""} ${
              formErrors.pinCode ? "error" : ""
            }`}
          />
          <label className={formErrors.pinCode ? "error" : ""}>
            PIN code / Zip Code
          </label>
          {!isViewOnly && <FaPenSquare className={"icon"} />}
        </div>

        {/* Contact No */}
        <div className="form-group">
          <input
            type="text"
            name="contactNo"
            value={
              formData.contactNo === "NotAvailable"
                ? "1234567890"
                : formData.contactNo || ""
            }
            disabled={isViewOnly}
            onChange={handleContactChange}
            // className={formErrors.contactNo ? "has-value error" : "has-value"}
            className={`${formData.contactNo ? "has-value" : ""} ${
              formErrors.contactNo ? "error" : ""
            }`}
          />
          <label className={formErrors.contactNo ? "error" : ""}>
            Contact no
          </label>
          {!isViewOnly && <FaPenSquare className={"icon"} />}
        </div>
      </div>

      {/* Buttons */}
      <div className="form-actions">
        {/* {!isViewOnly && !isEditOnly && !isApproved && (
          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleApprove}
          >
            Approve
          </button>
        )} */}

        {!isViewOnly && (
          <button type="submit" className="btn btn-primary">
            Update & Next
          </button>
        )}
      </div>

      <ToastContainer />
    </form>
  );
};

export default PersonalInfo;
