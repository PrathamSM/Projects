import React, { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { Link, NavLink, Outlet } from "react-router-dom";
import logo from "./../assets/group4.png";
import dashboardIcon from "./../assets/dashboard.png";
import smeIcon from "./../assets/sme-icon.png";
import projectsIcon from "./../assets/project.png";
import reportsIcon from "./../assets/report.png";
import ratingsIcon from "./../assets/ratings-icon.png";
import academianlogo from "./../assets/alogo.png";
import Upload from "./Upload";
import ProfileCard from "./ProfileCard";
import Project from "./project/Project";
import CreateProject from "./project/CreateProject";
 
const Sidebar = () => {
  
  const [role, setRole] = useState(null);
 

  useEffect(() => {
    const storedRole = localStorage.getItem("role")?.toUpperCase();
    setRole(storedRole);

    
  }, []);

  const isProfileVisible = useSelector((state) => state.popup.isVisible);

  
  useEffect(() => {
    document.body.style.overflow = isProfileVisible ? "hidden" : "auto";
  }, [isProfileVisible]);

  // Kg 12/01 Freeze scrolling when the ProfileCard is visible
  useEffect(() => {
    document.body.style.overflow = isProfileVisible ? "hidden" : "auto";
  }, [isProfileVisible]);

  return (
    <div className="flex h-screen bg-gray-50">
      <aside
        className="w-20 bg-gray-800 text-white flex flex-col items-center py-4"
        style={{ height: `700px` }} // Dynamically set the height
      >
        {/* Navigation Links */}
        <nav className="flex flex-col space-y-4">
          <NavLink
            to="/dashboard"
            className={({ isActive }) =>
              `flex flex-col items-center group cursor-pointer ${
                isActive ? "bg-gray-700 text-white" : ""
              }`
            }
          >
            <img src={dashboardIcon} alt="Dashboard" className="w-4 h-4 mt-0" />
            <span className="text-xs mt-2 group-hover:text-gray-300">
              Dashboard
            </span>
            </NavLink>

          {(role === "ADMIN" || role === "RES_MANAGER") && (
          <NavLink
              to="/sme"
              className={({ isActive }) =>
                `flex flex-col items-center group cursor-pointer ${
                  isActive ? "bg-gray-700 text-white" : ""
                }`
              }
            >
              <img src={smeIcon} alt="SME" className="w-4 h-4 mt-0" />
              <span className="text-xs mt-2 group-hover:text-gray-300">Resource</span>
            </NavLink>
          )}

         {/* Projects - Only visible to ADMIN & PROJ_MANAGER */}
         {(role === "ADMIN" || role === "PROJ_MANAGER") && (
           <NavLink
           to="/create-project"
           className={({ isActive }) =>
             `flex flex-col items-center group cursor-pointer ${
               isActive ? "bg-gray-700 text-white" : ""
             }`
           }
         >
              <img src={projectsIcon} alt="Projects" className="w-4 h-4 mt-0" />
              <span className="text-xs mt-2 group-hover:text-gray-300">Projects</span>
              </NavLink>
          )}
          <NavLink
            to="/reports"
            className={({ isActive }) =>
              `flex flex-col items-center group cursor-pointer ${
                isActive ? "bg-gray-700 text-white" : ""
              }`
            }
          >
            <img src={reportsIcon} alt="Reports" className="w-4 h-4 mt-0" />
            <span className="text-xs mt-2 group-hover:text-gray-300">
              Reports
            </span>
          </NavLink>
          {/* <NavLink
            to="/ratings"
            className={({ isActive }) =>
              `flex flex-col items-center group cursor-pointer ${
                isActive ? "bg-gray-700 text-white" : ""
              }`
            }
          >
            <img src={ratingsIcon} alt="Ratings" className="w-4 h-4 mt-0" />
            <span className="text-xs mt-2 group-hover:text-gray-300">Ratings</span>
            </NavLink> */}
        </nav>

        {/* Footer Logo */}
        <div className="flex flex-col items-baseline justify-end group h-full">
          <img src={academianlogo} alt="Academian" className="h-4 w-auto" />
        </div>
      </aside>
      <main className="flex-1">
      <Outlet /> 
      {isProfileVisible && <ProfileCard />}
      {/* This will render the active route component */}
      </main>
    </div>
  );
};

export default Sidebar;
