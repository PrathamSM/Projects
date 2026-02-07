import React, { useEffect, useState } from "react";
import axios from "axios";
import { MdModeEditOutline } from "react-icons/md";


const ManageProject = ({recProjectId}) => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  
  // Function to fetch projects
  const fetchProjects = () => {
    const url = recProjectId
      ? `http://localhost:8044/managepro/${recProjectId}`
      : "http://localhost:8044/managepro/details";

    axios
      .get(url)
      .then((response) => {
        setProjects(Array.isArray(response.data) ? response.data : [response.data]); // Handle single project response
      })
      .catch((error) => console.error("Error fetching data:", error));
  };

  useEffect(() => {
    fetchProjects();
  }, []);


  // Handle project closure
  const handleCloseProject = (projectId) => {
    axios
      .delete(`http://localhost:8045/api/assignments/del/${projectId}`)
      .then(() => {
        setProjects((prevProjects) =>
          prevProjects.map((proj) =>
            proj.projectId === projectId
              ? { ...proj, status: "Closed" }
              : proj
          )
        );
        setSelectedProject(null);
      })
      .catch((error) => console.error("Error closing project:", error));
  };

  return (
    <div className="p-8 bg-inherit min-h-screen">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Manage Projects</h2>
      <p className="text-[#4F626E] mb-6">
        Easily manage your projects, track milestones, and ensure everything is moving forward as planned.
      </p>


      <div className="max-w-8xl mx-auto bg-white p-6 shadow-md rounded-[10px] h-[80vh]">
        <div className="overflow-hidden">
          <table className="w-full table-auto border border-gray-300 rounded-[6px] shadow ">
            <thead className="bg-[#D9E3E9] text-gray-700 border-[#D9E3E9]">
              <tr className="text-left">
                {["Project ID", 
                "Project Name", 
                "Start Date", 
                "End Date", 
                "Resources Required",
                "Resources Assigned", 
                "Cost", "Contact", 
                "Skill", 
                "Status", 
                "Actions"].map((heading, index) => (
                <th key={index}       
                className={`text-center p-4 border text-sm whitespace-nowrap ${(index === 2 || index === 3) ? 'px-6' : ''}`}
                >{heading}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {projects.map((project) => (
                <tr key={project.projectId} className="text-center bg-white hover:bg-gray-100 border-[#fff] text-gray-700">
                  <td className="p-2  font-medium">{project.projectId}</td>
                  <td className="p-2  font-medium">{project.projectName}</td>
                  <td className="p-2  font-normal">{project.projectStartDate}</td>
                  <td className="p-2  font-normal">{project.projectEndDate}</td>
                  <td className="p-2  font-normal">{ project.numberOfSMEs}</td>
                  <td className="p-2  font-normal">{project.profileCount}</td>
                  <td className="p-2  font-normal">${project.plannedBudget}</td>
                  <td className="p-2  font-normal">{project.projectContactPerson}</td>
                  <td className="p-2  font-normal">{project.firstRequiredSkill}</td>
                  <td className="p-2  font-normal">
                    <span
                      className={`px-6 py-1 text-xs font-semibold ${
                        project.status === "Closed" ? "bg-red-100 text-red-700" : "bg-green-100 text-green-700"
                      }`}
                    >
                      {project.status === "Closed" ? "Closed" : "Active"}
                    </span>
                  </td>
                  <td className="p-2 text-sm relative">
                    {project.status !== "Closed" && (
                      <div className="relative inline-block text-left">
                        <button
                          onClick={() => setSelectedProject(selectedProject === project.projectId ? null : project.projectId)}
                          className="text-gray-700 px-3 py-1 rounded text-xs"
                        >
                          <MdModeEditOutline className="inline-block text-xl" />
                        </button>
                        {selectedProject === project.projectId && (
                          <div className="absolute bottom-10 right-0 mt-2 w-40 bg-gray-100 border rounded shadow-lg z-10">
                            <button
                              onClick={() => handleCloseProject(project.projectId)}
                              className="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                            >
                              Close Request
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                  </td>
                </tr>
              ))}
              {projects.length === 0 && (
                <tr>
                  <td colSpan="11" className="text-center text-gray-500 p-4">
                    No projects found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ManageProject;