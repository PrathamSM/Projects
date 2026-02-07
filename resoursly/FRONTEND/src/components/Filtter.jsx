import React, { useEffect, useState } from "react";
import CustomDropdown from "./CustomDropdown";
import axios from "axios";
import { IoIosSearch } from "react-icons/io";

/**
 * FilterBar (Tailwind version with custom dropdowns)
 * @param {Object} props
 * @param {Function} props.onFilterChange - Callback to notify parent of filter changes
 */
function FilterBar({ onFilterChange, onSearchCallback }) {
  // State for each filter
  const [vertical, setVertical] = useState("");
  const [role, setRole] = useState("");
  const [location, setLocation] = useState("");
  const [skill, setSkill] = useState("");
  const [verticalOptions, setVerticalOptions] = useState([]);
  const [rolesOptions, setRolesOptions] = useState([]);
  const [countryOptions, setCountryOptions] = useState([]);
  // Example filter options
  const getVerticalOptions = async () => {
    try {
      let res = await axios.get("http://localhost:8081/profiles/verticals");
      console.log(res.data.verticals);
      setVerticalOptions(res.data.verticals);
    } catch (error) {
      console.log(error);
    }
  };

  const getRoleOptions = async () => {
    try {
      let res = await axios.get("http://localhost:8081/profiles/roles");
      console.log(res.data.roles);
      setRolesOptions(res.data.roles);
    } catch (error) {
      console.log(error);
    }
  };
  const getCountryOptions = async () => {
    try {
      let res = await axios.get("http://localhost:8081/profiles/countries");
      console.log(res.data);

      // Convert ["USA"] → [{ label: "USA", value: "USA" }]
      const formattedCountries = res.data.map((country) => ({
        id: country,
        countryName: country,
      }));

      setCountryOptions(formattedCountries);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getVerticalOptions();
    getRoleOptions();
    getCountryOptions();
  }, []);
  // const verticalOptions = ["Finance", "Healthcare", "Retail", "Education"];
  //const roleOptions = ["Developer", "Designer", "Manager", "Analyst"];
  //const locationOptions = ["New York", "London", "Tokyo", "Remote"];
  const skillOptions = ["React", "Vue", "Angular", "Node.js"];

  // Handlers
  const handleVerticalSelect = (value) => {
    setVertical(value);
    onFilterChange({ vertical: value, role, location, skill });
  };

  const handleRoleSelect = (value) => {
    setRole(value);
    onFilterChange({ vertical, role: value, location, skill });
  };

  const handleLocationSelect = (value) => {
    setLocation(value);
    // onFilterChange({ vertical, role, location: value, skill });
  };

  const handleSkillSelect = (value) => {
    setSkill(value);
    onFilterChange({ vertical, role, location, skill: value });
  };

  const sendCallBack = () => {
    console.log(role, vertical, location);
    // debugger;
    onSearchCallback({ roles: role, vertical: vertical, country: location });
  };

  const clearFilter = () => {
    onSearchCallback({ roles: "", vertical: "", country: "" });
    setVertical("");
    setRole("");
    setLocation("");
  };

  return (
    <div className="flex flex-wrap gap-4 mb-4">
      {/* Vertical Filter */}
      <CustomDropdown
        placeholder="Verticals"
        options={verticalOptions}
        selected={vertical}
        onSelect={handleVerticalSelect}
        keyname={"verticalName"}
      />
      {/* Role Filter */}
      <CustomDropdown
        placeholder="Roles"
        options={rolesOptions}
        selected={role}
        onSelect={handleRoleSelect}
        keyname={"roleName"}
      />
      {/* Location Filter */}
      <CustomDropdown
        placeholder="Location"
        options={countryOptions}
        selected={location}
        onSelect={handleLocationSelect}
        keyname={"countryName"}
      />
      <div
        className="flex justify items-center"
        onClick={(e) => {
          e.preventDefault(), sendCallBack();
        }}
      >
        {" "}
        <IoIosSearch />
      </div>

      <span className="text-[#a0b2bd] flex align-middle mt-auto mb-auto">
        |
      </span>
      <button
        className="text-[#dc2626] text-[14px]"
        style={{ fontFamily: "roboto", fontWeight: "400" }}
        onClick={(e) => {
          e.preventDefault(), clearFilter();
        }}
      >
        {" "}
        Clear all
      </button>
      {/* Skill Filter */}
      {/* <CustomDropdown
        placeholder="Skill"
        options={skillOptions}
        selected={skill}
        onSelect={handleSkillSelect}
      /> */}
    </div>
  );
}

export default FilterBar;
