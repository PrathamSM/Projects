import React, { useState } from "react";
 
import axios from "axios";
 
import backgroundImage from "../../assets/sme.png";
 
import academian from "../../assets/alogo.png";
 
import group from "../../assets/group4.png";
 
import { jwtDecode } from "jwt-decode";
 
import { useNavigate } from "react-router-dom";
 
import { toast } from "react-toastify";
 
import "react-toastify/dist/ReactToastify.css";
 
import Group from "../../assets/Group.svg";
import { FaEye, FaEyeSlash } from "react-icons/fa";
 
const LoginPage = () => {
  const [email, setEmail] = useState("");
 
  const [password, setPassword] = useState("");
 
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
 
  const navigate = useNavigate();
 
  const handleLogin = async (e) => {
    e.preventDefault();
 
    setError("");
 
    try {
      const response = await axios.post("http://localhost:8075/auth/login", {
        email,
 
        password,
      });
 
      const { token } = response.data;
 
      console.log("Received Token:", token);
 
      const decodedToken = jwtDecode(token);
 
      const role = decodedToken.role;
 
      localStorage.setItem("token", token);
 
      localStorage.setItem("role", role);
 
      localStorage.setItem("userDisplayName", "Ramneek Sehgal");
 
      if (role == "ADMIN") {
        localStorage.setItem("userDisplayName", "Admin");
      }
 
      if (role == "RES_MANAGER") {
        localStorage.setItem("userDisplayName", "Mgrigank Bhusan");
      }
 
      if (role == "PROJ_MANAGER") {
        localStorage.setItem("userDisplayName", "Himanshu Jain");
      }
 
      localStorage.setItem("token", token);
 
      localStorage.setItem("role", role);
 
      // toast.success("Login Successful!", { autoClose: 2000 });
 
      setTimeout(() => {
        if (role === "PROJ_MANAGER") {
          navigate("/create-project");
        } else if (role === "ADMIN") {
          navigate("/dashboard");
        } else if (role === "RES_MANAGER") {
          navigate("/sme");
        }
      }, 1000); // Delay to allow toast display
    } catch (err) {
      console.error("Login Error:", err); // Log error for debugging
 
      if (err.response) {
        if (err.response.status === 400) {
          setError("Invalid email or password. Please try again.");
        } else {
          setError(
            `Error: ${err.response.data.message || "Something went wrong."}`
          );
        }
      } else {
        setError("Unable to connect. Please check your network.");
      }
    }
  };
 
  return (
    <div className="flex h-screen w-full">
      {/* Left Section with Background Image */}
      <div
        className="w-1/2 flex flex-col items-center justify-center text-center text-white relative bg-cover bg-center"
        style={{ backgroundImage: `url(${Group})` }}
      >
        {/* Academian Branding
<div className="absolute top-6 left-6 flex items-center gap-x-2">
<img src={academian} alt="Academian Logo" className="h-6" />
 
 
      </div> */}
        <div className="flex items-center gap-x-3">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="62"
            height="70"
            viewBox="0 0 62 70"
            fill="none"
          >
            <path
              d="M37.3069 9.3922C32.6278 14.5783 28.0865 12.5752 25.3464 9.82742C24.3184 8.79651 22.7035 8.4184 21.4752 9.20002C20.3894 9.891 19.9708 11.2657 20.3933 12.4814C26.1367 29.0063 14.6585 35.406 6.37717 36.9133C5.1545 37.1358 4.20508 38.1612 4.20508 39.404C4.20508 40.7276 5.30211 41.7835 6.55737 42.2034C7.8861 42.6478 8.93504 43.3568 9.41867 43.7867C13.2395 46.9058 13.1265 50.7392 12.2406 53.2436C11.8951 54.2203 12.1662 55.3938 13.0657 55.9078C14.2615 56.5911 16.2869 55.808 17.1904 54.7685C18.6123 53.1324 20.7596 51.4222 22.0803 50.4899C33.3173 44.4392 41.6075 50.2385 45.4774 55.2812C46.3432 56.4095 47.917 56.8472 49.1429 56.1261C50.268 55.4643 50.6716 54.0577 50.2954 52.8078C48.3988 46.5044 52.553 43.4161 56.1838 42.2638C57.3178 41.9039 58.203 40.9154 58.203 39.7257C58.203 38.3576 57.047 37.2755 55.6911 37.0934C52.8795 36.7159 50.292 35.6125 49.1413 34.9733C38.3589 27.8137 39.1588 17.8882 41.5347 12.1767C42.0649 10.9021 41.6402 9.3594 40.413 8.72722C39.366 8.18785 38.0959 8.51775 37.3069 9.3922Z"
              fill="url(#paint0_linear_137_1757)"
            />
            <path
              d="M56.3551 27.2816C49.5242 25.8224 48.9883 20.888 49.9979 17.1411C50.3767 15.7354 49.8967 14.1478 48.6057 13.4749C47.4644 12.88 46.0645 13.2048 45.223 14.1786C33.7836 27.4149 22.5023 20.6744 17.0563 14.2562C16.2522 13.3086 14.8894 12.9991 13.8132 13.6205C12.6669 14.2823 12.301 15.7603 12.565 17.0573C12.8445 18.4302 12.7549 19.6931 12.6244 20.3269C11.8337 25.1954 8.45733 27.0142 5.84547 27.4992C4.82689 27.6884 3.94616 28.5099 3.95077 29.5458C3.95691 30.9232 5.64777 32.2857 6.99983 32.5483C9.1277 32.9617 11.6824 33.9662 13.1501 34.6438C24.0086 41.35 23.1314 51.4291 20.6993 57.3019C20.1551 58.6159 20.5629 60.1977 21.8003 60.8988C22.936 61.5422 24.3559 61.1885 25.2504 60.2378C29.7609 55.4435 34.5126 57.497 37.3259 60.0653C38.2045 60.8674 39.5032 61.1398 40.5335 60.5449C41.7184 59.8609 42.0775 58.3187 41.5572 57.0533C40.4783 54.4297 40.1401 51.6371 40.1184 50.321C40.9275 37.4034 49.9233 33.1334 56.0576 32.3352C57.4265 32.1571 58.5501 31.0179 58.484 29.6391C58.4276 28.4626 57.5069 27.5277 56.3551 27.2816Z"
              fill="url(#paint1_linear_137_1757)"
            />
            <circle cx="31.142" cy="66.1312" r="3.724" fill="#0199FF" />
            <circle cx="58.2025" cy="50.4901" r="3.724" fill="#00EB86" />
            <circle cx="4.32751" cy="50.4901" r="3.724" fill="#00EB86" />
            <circle cx="31.142" cy="4.06384" r="3.724" fill="#00EB86" />
            <circle cx="58.2025" cy="19.2079" r="3.724" fill="#0199FF" />
            <circle cx="4.32751" cy="19.2079" r="3.724" fill="#0199FF" />
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M39.9121 19.2908C33.0183 23.9765 26.5057 22.3273 21.6767 19C22.5795 27.4129 18.1795 32.3755 13 35.0472C13.0009 35.0476 13.0018 35.0481 13.0028 35.0485C20.2885 39.5481 22.2908 45.5662 22.085 50.8133C29.4697 46.8979 35.5728 48.0998 39.973 50.8156C39.9722 50.7849 39.9716 50.7549 39.9711 50.7257C40.4896 42.4478 44.37 37.721 48.7049 35.1835C41.6833 30.3965 39.729 24.4177 39.9121 19.2908Z"
              fill="#016FFE"
            />
            <defs>
              <linearGradient
                id="paint0_linear_137_1757"
                x1="31.2041"
                y1="7.78809"
                x2="31.2041"
                y2="57.4414"
                gradientUnits="userSpaceOnUse"
              >
                <stop stop-color="#019AFF" />
                <stop offset="1" stop-color="#01FAFF" />
              </linearGradient>
              <linearGradient
                id="paint1_linear_137_1757"
                x1="19.6929"
                y1="10.2258"
                x2="46.6918"
                y2="56.9894"
                gradientUnits="userSpaceOnUse"
              >
                <stop stop-color="#01A0E9" />
                <stop offset="1" stop-color="#01EB86" />
              </linearGradient>
            </defs>
          </svg>
          <p className="text-3xl font-extrabold mt-4">Resourcely</p>
        </div>
        <p className="text-gray-200 text-sm mt-2 w-4/5">
          Connecting skill to success
        </p>
 
        {/* Academian Branding */}
 
        {/* Academian Branding */}
        <div className="absolute bottom-6 left-1/2 transform -translate-x-1/2 flex items-center gap-x-2">
          <h4>Powered by</h4>
          <img src={academian} alt="Academian Logo" className="h-6" />
        </div>
      </div>
 
      <div className="w-1/2 flex justify-center items-center p-6 border-l border-gray-300">
        <div className="w-full max-w-md">
          <div className="text-center">
            <h2 className="text-3xl font-semibold  mb-6 text-gray-800">
              Sign in to your account
            </h2>
          </div>
 
          <form className="w-full max-w-md" onSubmit={handleLogin}>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-normal leading-5 tracking-normal mb-2">
                Email Address
              </label>
              <input
                type="email"
                className="w-full px-4 py-2 border rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter email address"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-normal leading-5 tracking-normal mb-2">
                Password
              </label>
              <div className="relative w-full">
                <input
                  type={showPassword ? "" : "password"}
                  className={`w-full px-4 py-2 border rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 pr-10 ${showPassword ? '!border' : ''}`}
 
                  placeholder="Enter password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-3 flex items-center text-gray-500"
                  onClick={() => setShowPassword((prev) => !prev)}
                >
                  {showPassword ? (
                    <FaEyeSlash size={18} />
                  ) : (
                    <FaEye size={18} />
                  )}
                </button>
              </div>
            </div>
            {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
            >
              Sign In
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};
 
export default LoginPage;