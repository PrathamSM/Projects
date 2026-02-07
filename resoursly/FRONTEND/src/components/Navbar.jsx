import React from "react";
// import mainIcon from "./../assets/group4.png";
import { useNavigate } from "react-router-dom";
import mainIcon from "./../assets/Resourcely.png";
import UserImg from "../assets/dp.svg";
function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    console.log("Logging out...");
    localStorage.clear();
    sessionStorage.clear();
 
    // Clear cookies
    document.cookie.split(";").forEach((c) => {
      document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/");
    });
 
    setTimeout(() => {
      window.location.href = "/login"; // Force logout by reloading the page
    }, 500);
  };
 
  return (
    <nav className="bg-gray-700 shadow-md h-[40px] w-[full]">
      <div className="mx-auto  px-4 sm:px-6 lg:px-8">
      <div className="absolute top-0 left-0">
            <img
               src={mainIcon}
               alt="mainIcon"
               className="w-[120px] h-[25px] m-2"
            />
</div>
        <div className="relative flex h-10 items-center justify-between w-full">
          {/* Search Bar */}
          <div className="w-full">
            <div className="relative w-70 ">
              {/* <input
                type="text"
                className="block w-full rounded-full border border-gray-300 bg-gray-100 py-2 pl-10 pr-3 h-6 text-sm placeholder-gray-500 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
                placeholder="Search SME's, Projects..."
              /> */}
              <div className="absolute inset-y-0 left-0 flex items-center pl-3">
                {/* <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-5 w-5 text-gray-400"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                > */}
                  {/* <path
                    fillRule="evenodd"
                    d="M12.9 14.32a8 8 0 111.414-1.414l4.387 4.387a1 1 0 01-1.414 1.414l-4.387-4.387zm-4.9-1.82a6 6 0 100-12 6 6 0 000 12z"
                    clipRule="evenodd"
                  />
                </svg> */}
              </div>
            </div>
          </div>

          {/* Profile Section */}
          <div className="flex items-center">
            <button
              type="button"
              className="relative flex rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800]"
              id="user-menu-button"
              aria-expanded="false"
              aria-haspopup="true"
            >
              <span className="sr-only">Open user menu</span>
              <img
                className="h-6 w-6 rounded-full"
                src={UserImg}
                alt="User Avatar"
              />
            </button>
            <button
          onClick={handleLogout}
          className="bg-gray-500 text-white ml-2 px-2 py-1 rounded text-sm hover:bg-blue-700 transition"
        >
          Logout
        </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
