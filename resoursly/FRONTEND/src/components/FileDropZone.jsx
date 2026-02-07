import React, { useState, useRef } from "react";
import axios from "axios";

const FileDropZone = ({ onUploadSuccess }) => {
  const [selectedFiles, setSelectedFiles] = useState(null);
  const [isDragging, setIsDragging] = useState(false); // State to track drag-over state
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    setSelectedFiles(e.target.files);
  };

  const handleBrowseClick = () => {
    fileInputRef.current.click();
  };

  const handleRemoveFile = () => {
   setSelectedFiles(null); // Clear the selected file manually
    if (selectedFiles) {
      const filesArray = Array.from(selectedFiles);
      filesArray.splice(index, 1);
      setSelectedFiles(filesArray.length > 0 ? filesArray : null);
    }
  };

  const handleUpload = async () => {
    if (!selectedFiles || selectedFiles.length === 0) {
      alert("Please select files to upload.");
      return;
    }

    const formData = new FormData();
    Array.from(selectedFiles).forEach((file) => {
      formData.append("files", file);
    });

    try {
      const response = await axios.post(
        "http://localhost:8088/api/resume/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      console.log("Upload successful:", response.data);
      if (onUploadSuccess) onUploadSuccess(response.data); // Pass new profiles to the parent component
     
      // Clear selected files after upload
      setSelectedFiles(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = ""; // Reset the file input
      }
     // alert("file will upload on click of OK ");
    } catch (error) {
      console.error("Error in file upload:", error.response?.data || error.message);
      alert("Failed to upload files.");
    }
  };

  // Drag and Drop Handlers
  const handleDragOver = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(true); // Set dragging state
  };

  const handleDragLeave = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false); // Reset dragging state
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false); // Reset dragging state

    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      setSelectedFiles(e.dataTransfer.files); // Set dropped files
      e.dataTransfer.clearData();
    }
  };

  return (
    <div className="bg-custom-light flex flex-col gap-4 sm:gap-6">
      <h2 className="text-left text-lg font-semibold text-gray-700">
        Upload Profile
      </h2>
      <p className="text-left text-sm text-gray-500">
        This will help us accurately assess their expertise and match them with relevant opportunities.
      </p>

      <div
        className={`border border-dashed ${
          isDragging ? "border-blue-500 bg-blue-100" : "border-gray-400"
        } rounded-md flex flex-col sm:flex-row sm:items-center justify-between p-2 sm:gap-4 bg-white max-w-screen-sm`}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
      >
        <div className="flex flex-col sm:flex-row h-20 items-center justify-between w-full">
          <p className="text-gray-600 text-sm sm:text-base">
          &emsp;&emsp;Drag and drop your file &emsp;or&emsp;
            <span
              className="text-blue-600 font-semibold cursor-pointer"
              onClick={handleBrowseClick}
            >
              Browse
            </span>
          </p>
          <button
            onClick={handleUpload}
            className="bg-blue-600 text-white px-20 py-2 rounded hover:bg-blue-700 transition"
          >
            Upload
          </button>
        </div>
      </div>

      <input
        type="file"
        ref={fileInputRef}
        multiple
        className="hidden"
        onChange={handleFileChange}
      />
      <p className="text-left text-sm text-gray-500">
        Accepted file types: .docx,.pdf
      </p>

          {/* Selected File Display */}
          {selectedFiles && (
      <div className="flex items-center gap-4 mt-2">
        {Array.from(selectedFiles).map((file, index) => (
         <div key={index} className="flex items-center justify-between gap-4">
            <p className="text-sm text-blue-600">
              <span className="font-medium">{file.name}</span>
            </p>
            <button
              // onClick={handleRemoveFile}
              onClick={() => handleRemoveFile(index)}
              className="text-red-600 text-sm underline hover:text-red-800, font-bold text-base"
            >
              Remove
            </button>  
          </div>
        ))}
        </div>
  )}     
    </div>
  );
};

export default FileDropZone;
