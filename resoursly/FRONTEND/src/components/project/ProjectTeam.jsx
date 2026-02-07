import axios from "axios";
import React, { useState, useEffect } from "react";
import { AiOutlineTeam } from "react-icons/ai";
import { MdDeleteOutline } from "react-icons/md";
import { FiEdit } from "react-icons/fi";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function ProjectTeam({ projectId, onSave }) {
  console.log("Received projectId in ProjectTeam:", projectId);

  const [teamList, setTeamList] = useState([]);
  const [editIndex, setEditIndex] = useState(null);
  const [formData, setFormData] = useState({
    projectId: projectId,
    projectManagerNames: [],
    projectManagerEmails: [],
    projectManagerPhones: [],
  });

  const [projectManager, setProjectManager] = useState({
    name: "",
    email: "",
    phone: "",
  });

  useEffect(() => {
    console.log("Updated projectId in ProjectTeam:", projectId);
  }, [projectId]);

  const handleChange = (e) => {
    setProjectManager({ ...projectManager, [e.target.name]: e.target.value });
  };

  const handleAdd = () => {
    // if (!projectManager.name || !projectManager.email || !projectManager.phone) {
    //   toast.error("All fields are required!");
    //   return;
    // }
    if (!validate()) return;

    let updatedList = [...teamList];
    if (editIndex !== null) {
      updatedList[editIndex] = projectManager;
      setEditIndex(null);
    } else {
      updatedList.push(projectManager);
    }

    setTeamList(updatedList);
    setProjectManager({ name: "", email: "", phone: "" });

    setFormData({
      projectManagerNames: updatedList.map((member) => member.name),
      projectManagerEmails: updatedList.map((member) => member.email),
      projectManagerPhones: updatedList.map((member) => member.phone),
    });
  };

  const handleEdit = (index) => {
    setProjectManager(teamList[index]);
    setEditIndex(index);
  };

  const handleRemove = (index) => {
    const updatedList = teamList.filter((_, i) => i !== index);
    setTeamList(updatedList);
    setEditIndex(null);

    setFormData({
      projectManagerNames: updatedList.map((member) => member.name),
      projectManagerEmails: updatedList.map((member) => member.email),
      projectManagerPhones: updatedList.map((member) => member.phone),
    });
  };

  // Validate Form Data
  const validate = () => {
    const errors = {};
  
    if (!projectManager.name) {
      errors.name = "Manager Name is required.";
    }
  
    if (!projectManager.email) {
      errors.email = "Email is required.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(projectManager.email)) {
      errors.email = "Invalid Email Format.";
    }
  
    if (!projectManager.phone) {
      errors.phone = "Phone Number is required.";
    } else if (!/^\d{10}$/.test(projectManager.phone)) {
      errors.phone = "Phone Number must be 10 digits.";
    }
  
    if (Object.keys(errors).length > 0) {
      Object.values(errors).forEach((err) => toast.error(err));
      return false;
    }
  
    return true;
  };
  

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!projectId) {
      toast.error("Project ID is missing!");
      return;
    }

    if (teamList.length === 0) {
      toast.error("Please add at least one project manager!");
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8041/project-team/create",
        {
          projectId,
          ...formData,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Post response:", response.data);
      toast.success("Project team data posted successfully!");
      onSave();
    } catch (error) {
      console.error("Error posting ProjectTeam:", error.response?.data || error.message);
      toast.error("Failed to post Project Team.");
    }
  };

  return (
    <div>
      <h3 className="text-lg font-semibold text-blue-500 mb-4 flex items-center gap-2">
        <AiOutlineTeam className="text-blue-500" /> Project Team
      </h3>
      <form id="ProjectTeamForm" onSubmit={handleSubmit} className="space-y-8">
        <div className="grid grid-cols-4 sm:grid-cols-3 gap-4 items-center">
          <input
            type="text"
            name="name"
            placeholder="Project Manager Name*"
            value={projectManager.name}
            onChange={handleChange}
            className="p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
          />
          <input
            type="email"
            name="email"
            placeholder="Project Manager Email*"
            value={projectManager.email}
            onChange={handleChange}
            className="p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
          />
          <input
            type="tel"
            name="phone"
            placeholder="Project Manager Phone*"
            value={projectManager.phone}
            onChange={handleChange}
            className="p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-full"
          />
        </div>

        <div className="flex justify-end mt-4">
          <button
            type="button"
            onClick={handleAdd}
            className="bg-blue-500 text-white px-4 py-2 text-sm rounded-md hover:bg-blue-600 transition"
          >
            {editIndex !== null ? "Update" : "Add"}
          </button>
        </div>

        <h3 className="mt-8 text-lg font-medium text-gray-700 border-b pb-2">Added Project Managers</h3>
        {teamList.length === 0 ? (
          <p className="text-gray-500 mt-2 text-center">No Information added yet!</p>
        ) : (
          <div className="mt-4 border border-gray-300 rounded-md p-4 bg-gray-50 w-full flex flex-col items-center">
            {teamList.map((manager, index) => (
              <div key={index} className="grid grid-cols-1 sm:grid-cols-4 gap-4 text-center w-full py-3 border-b last:border-none">
                <span className="font-medium text-gray-800">{manager.name}</span>
                <span className="text-blue-500">{manager.email}</span>
                <span>{manager.phone}</span>
                <div className="flex gap-6 justify-center">
                  <button type="button" onClick={() => handleEdit(index)} className="text-blue-500 hover:underline">
                    <FiEdit size={20} />
                  </button>
                  <button type="button" onClick={() => handleRemove(index)} className="text-red-500 hover:underline">
                    <MdDeleteOutline size={22} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
        {/* <h3 className="mt-8 text-lg font-medium text-gray-700 border-b pb-2">Assigned SMEs</h3>
        <p className="text-gray-500 mt-2 text-center">No SME are assigned currently!</p> */}
        <ToastContainer />
      </form>
    </div>
  );
}
