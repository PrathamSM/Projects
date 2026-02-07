import React, { useState } from "react";
import { IoClose } from "react-icons/io5";
import profileStatusClient from "../AxiosClient/profileStatusClient";
import { useSelector } from 'react-redux';


const NDAConfirmation = ({ onConfirm , isFileUploadeds, onClose}) => {
  const currentProfileId = useSelector((state) => state.activeProfile.proId);
  const [selectedFile, setSelectedFile] = useState(null);
  const [isFileUploaded, setIsFileUploaded] = useState(false);
  const [uploadMessage, setUploadMessage] = useState("");

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedFile(file);
      console.log("File selected:", file.name);
      await uploadFile(file);
    }
  };
  const handleContinue = async (e) =>{
    onConfirm(e);

    if (isFileUploadeds) {
    isFileUploadeds(true); // Update UI instantly

    try {
      await profileStatusClient.put(`${currentProfileId}`, {
        ndaSigned: true,  // Persist NDA signed status
      });
  
      console.log("NDA status updated successfully in the database");
    } catch (error) {
      console.error("Error updating NDA status:", error);
    }

  }

  }
  const handleDrop = async (event) => {
    event.preventDefault();
    const file = event.dataTransfer.files[0];
    if (file) {
      setSelectedFile(file);
      console.log("File dropped:", file.name);
      await uploadFile(file);
    }
  };

  const uploadFile = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:8080/upload", {
        method: "POST",
        body: formData,
      });

      const result = await response.text();
      console.log("Upload response:", result);

      if (response.ok) {
        setIsFileUploaded(true);
        setUploadMessage(result);
        
      } else {
        setIsFileUploaded(false);
        setUploadMessage("Upload failed: " + result);
      }
    } catch (error) {
      console.error("Upload error:", error);
      setIsFileUploaded(false);
      setUploadMessage("Upload error: " + error.message);
    }
  };

  return (
    <div className="fixed inset-0 flex justify-center items-center bg-gray-900 bg-opacity-50">
      <div className="bg-white p-8 rounded-lg shadow-lg w-2/3 max-w-lg text-center">
      <div className="flex justify-between" > 
        <h2 className="text-lg font-semibold mb-4">Confirm NDA status before continuing</h2>
         
        {onClose && <IoClose className="text-lg text-red-700 cursor-pointer" onClick={()=>{onClose()}} />}
        </div>

        {/* Drag & Drop Area */}
        <div
          className="border-dashed border-2 border-blue-500 p-10 rounded-lg cursor-pointer h-40 flex flex-col justify-center items-center"
          onDragOver={(e) => e.preventDefault()}
          onDrop={handleDrop}
        >
          <label className="flex flex-col items-center text-gray-600 cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="50" viewBox="0 0 24 30" fill="none">
              <path d="M14.8379 3.29208V8.63532C14.8379 8.88675 14.8379 9.15424 14.8623 9.2191C14.8785 9.25154 14.9109 9.28397 14.9433 9.30018C15.0082 9.32451 15.2677 9.32451 15.5271 9.32451H20.8704L14.8379 3.29208ZM15.5271 10.5407C15.0082 10.5407 14.7001 10.5407 14.392 10.3867C14.1244 10.2488 13.9136 10.0299 13.7758 9.76235C13.6217 9.46235 13.6217 9.14613 13.6217 8.63532V2.43262H5.31092C3.85957 2.43262 2.67578 3.6164 2.67578 5.06775V25.338C2.67578 26.7894 3.85957 27.9732 5.31092 27.9732H19.0947C20.5461 27.9732 21.7298 26.7894 21.7298 25.338V10.5407H15.5271ZM14.9433 9.30018C15.0082 9.32451 15.2677 9.32451 15.5271 9.32451H20.8704L14.8379 3.29208V8.63532C14.8379 8.88675 14.8379 9.15424 14.8623 9.2191C14.8785 9.25154 14.9109 9.28397 14.9433 9.30018Z" fill="#0B6EFD"/>
              <path d="M11.9189 15.4053L11.9189 23.5134M11.9189 15.4053L9.16211 17.9999M11.9189 15.4053L14.6756 17.9999" stroke="white" strokeWidth="1.21622" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
            {isFileUploaded ? (
              <span className="text-green-600 font-semibold mt-2">{selectedFile.name}</span>
            ) : (
              <span className="text-lg mt-2">Click to Upload NDA or drag your file here</span>
            )}
            <input type="file" className="hidden" onChange={handleFileChange} />
          </label>
        </div>

        {/* NDA Status & Continue Button */}
        <div className="mt-6 flex justify-between items-center">
          <span className={`px-4 py-2 text-sm rounded ${isFileUploaded ? "bg-green-500 text-white" : "bg-gray-300 text-gray-700"}`}>
            {isFileUploaded ? "NDA Uploaded" : "NDA Pending"}
          </span>
          <button
            className={`px-6 py-2 text-white rounded text-lg ${isFileUploaded ? "bg-blue-600 hover:bg-blue-700" : "bg-gray-400 cursor-not-allowed"}`}
            disabled={!isFileUploaded}
            onClick={(e)=>handleContinue(e)}
          >
            Continue
          </button>
        </div>
      </div>
    </div>
  );
};

export default NDAConfirmation;
