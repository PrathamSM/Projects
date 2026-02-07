// import { useState } from 'react'
import FileDropZone from './components/FileDropZone.jsx'
import UploadedSMEList from './components/UploadedSMEList.jsx'
import UserProfileCard from './components/UserProfileCard.jsx'
import Navbar from './components/Navbar.jsx'
import Sidebar from './components/Sidebar.jsx'
import { useSelector } from 'react-redux'
import ProfileCard from './components/ProfileCard.jsx'
import BudgetFinancial from './components/project/BudgetFinancial.jsx'
import DocumentUpload from './components/project/DocumentUpload.jsx'
import LoginPage from './components/login/LoginPage.jsx'
import Project from './components/project/Project.jsx'
import { BrowserRouter as Router, Route, Routes, Navigate, createBrowserRouter, RouterProvider } from "react-router-dom";
import { useEffect, useState } from "react";
import MainLayout from './components/MainLayout.jsx'
import Upload from './components/Upload.jsx'
import CreateProject from './components/project/CreateProject.jsx'
import Reports from './components/Reports.jsx'
import Dashboard from './components/dashboard 1.jsx'
import Dashboard2 from './components/dashboard2.jsx'

function App() {
  const [role, setRole] = useState(null);
  const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      { index: true, element: <Navigate to="/login" /> },
      { path: "dashboard", element:<Dashboard2/>  },
      { path: "sme", element: <Upload />},
      { path: "create-project", element: <Project
         /> },
      { path: "reports", element:<Reports/> },
      { path: "ratings", element: <h1>Ratings Page</h1> },
    ],
  },
  { path: "/profile", element: <ProfileCard /> },
  {path: "/login", element :localStorage.getItem("token") ?<Navigate to="/dashboard"/> : <LoginPage/>},
  { path: "*", element: <h1>404 - Page Not Found</h1> },
]);
useEffect(() => {
  setRole(localStorage.getItem("role")); // Get role from localStorage
}, []);
  return (
    // <>
    // {/* <Navbar/>
    // <Sidebar/> */}
    // <LoginPage/>

    <RouterProvider router={router} />
  );
}

export default App
