import { useState, useEffect } from "react";
import axios from "axios";

const RoleVerticledrop = ({ onSelectionChange }) => {
  // ✅ Accepts the function as a prop
  const [formData, setFormData] = useState({
    role: "",
    vertical: "",
  });

  const [roles, setRoles] = useState([]);
  const [verticals, setVerticals] = useState([]);

  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8081/profiles/roles"
        );
        console.log("Roles API Response:", response.data);
        setRoles(response.data?.roles || []); //  Ensure default empty array
      } catch (error) {
        console.error("Error fetching roles:", error);
        setRoles([]); // Set empty array on error
      }
    };

    const fetchVerticals = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8081/profiles/verticals"
        );
        console.log("Verticals API Response:", response.data);
        setVerticals(response.data?.verticals || []); //  Ensure default empty array
      } catch (error) {
        console.error("Error fetching verticals:", error);
        setVerticals([]); //  Set empty array on error
      }
    };

    fetchRoles();
    fetchVerticals();
  }, []);

  const handleChange = (e) => {
    const updatedFormData = {
      ...formData,
      [e.target.name]: Number(e.target.value), //  Convert to number
    };

    setFormData(updatedFormData);

    if (onSelectionChange) {
      onSelectionChange(updatedFormData);
    }
  };
  return (
    <div className="grid grid-cols-2 gap-4">
      {/* Role Dropdown */}
      <div>
        <label className="block text-sm font-medium my-2">Role</label>
        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        >
          <option value="">Select role</option>
          {roles.map((role) => (
            <option key={role.id} value={role.id}>
              {role.roleName}
            </option>
          ))}
        </select>
      </div>

      {/* Vertical Dropdown */}
      <div>
        <label className="block text-sm font-medium my-2">Vertical</label>
        <select
          name="vertical"
          value={formData.vertical}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        >
          <option value="">Select vertical</option>
          {verticals.map((vertical) => (
            <option key={vertical.id} value={vertical.id}>
              {vertical.verticalName}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default RoleVerticledrop;
