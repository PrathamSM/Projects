import { useState, useRef, useEffect } from "react";
import axios from "axios";
import { IoDocumentsOutline } from "react-icons/io5";
import { FaEye, FaDownload } from "react-icons/fa";
import { MdDelete } from "react-icons/md";

export default function DocumentUpload({ projectId, onSave }) {
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [uploadedFiles, setUploadedFiles] = useState([]);
  const fileInputRef = useRef(null);

  useEffect(() => {
    if (projectId) fetchUploadedFiles();
  }, [projectId]);

  const fetchUploadedFiles = async () => {
    try {
      const response = await axios.get(`http://localhost:8043/api/documents/project/${projectId}`);
      setUploadedFiles(response.data);
    } catch (error) {
      console.error("Error fetching uploaded files", error);
    }
  };

  const handleFileChange = (e) => {
    setSelectedFiles([...e.target.files]);
  };

  const handleBrowseClick = () => {
    fileInputRef.current.click();
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (selectedFiles.length === 0) {
      alert("Please select files to upload.");
      return;
    }
    
    const formData = new FormData();
    selectedFiles.forEach((file) => {
      formData.append("files", file);
    });

    try {
      await axios.post(`http://localhost:8043/api/documents/upload/${projectId}`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      //alert("Files uploaded successfully");
      setSelectedFiles([]);
      fileInputRef.current.value = "";
      fetchUploadedFiles(); // Refresh list after upload
      onSave();
    } catch (error) {
      console.error("Error uploading files", error);
    }
  };

  const handleRemoveFile = (index) => {
    if (selectedFiles) {
      const filesArray = Array.from(selectedFiles);
      filesArray.splice(index, 1);
      
      // Convert back to FileList
      const updatedFileList = new DataTransfer();
      filesArray.forEach((file) => updatedFileList.items.add(file));
  
      setSelectedFiles(updatedFileList.files.length > 0 ? updatedFileList.files : null);
    }
  };
  

  const handleDelete = async (fileId, fileIndex) => {
    try {
      await axios.delete(`http://localhost:8043/api/documents/delete/${fileId}/${fileIndex}`);
  
      setUploadedFiles((prevFiles) =>
        prevFiles
          .map((file) => {
            if (file.id === fileId) {
              const updatedFileNames = [...file.fileNames];
              const updatedFileTypes = [...file.fileTypes];
              const updatedFileSizes = [...file.fileSizes];
  
              // Remove the specific file using the index
              updatedFileNames.splice(fileIndex, 1);
              updatedFileTypes.splice(fileIndex, 1);
              updatedFileSizes.splice(fileIndex, 1);
  
              // If no files remain, remove the whole object; otherwise, update the entry
              return updatedFileNames.length
                ? { ...file, fileNames: updatedFileNames, fileTypes: updatedFileTypes, fileSizes: updatedFileSizes }
                : null;
            }
            return file;
          })
          .filter(Boolean) // Remove null entries (i.e., empty file objects)
      );
    } catch (error) {
      console.error("Error deleting file", error);
    }
  };

  // Drag and Drop Handlers
  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e) => {
    e.preventDefault();
    setIsDragging(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);
    const droppedFiles = Array.from(e.dataTransfer.files);
    setSelectedFiles([...selectedFiles, ...droppedFiles]);
  };
  
  

  return (
    <div className="flex flex-col gap-4 sm:gap-6">
      <h3 className="text-lg font-semibold text-blue-500 mb-4 flex items-center gap-2">
        <IoDocumentsOutline /> Documents
      </h3>
      <h2 className="text-left text-lg font-semibold text-gray-700">Project Document Upload</h2>
      <form onSubmit={handleUpload} className="space-y-8">
        <div className="border border-dashed border-gray-400 rounded-md p-2 bg-white max-w-screen-sm">
          <div className="flex flex-col sm:flex-row h-20 items-center justify-between w-full">
            
            <p className="text-gray-600 text-sm sm:text-base">
              To upload file click on
              <span className="text-blue-600 font-semibold cursor-pointer" onClick={handleBrowseClick}> Browse</span>
            </p>
            <button type="submit" className="bg-blue-600 text-white px-20 py-2 rounded hover:bg-blue-700">
              Upload
            </button>
          </div>
                  
      {/* Selected File Display */}
        </div>
        {selectedFiles && (
  <div className="flex items-center gap-4 mt-2">
    {Array.from(selectedFiles).map((file, index) => (
      <div key={index} className="flex items-center justify-between gap-4">
        <p className="text-sm text-blue-600">
          <span className="font-medium">{file.name}</span>
        </p>
        <button
          onClick={() => handleRemoveFile(index)}
          className="text-red-600 text-sm underline hover:text-red-800 font-bold text-base"
        >
          Remove
        </button>
      </div>
    ))}
  </div>
)}
        <input type="file" ref={fileInputRef} multiple className="hidden" onChange={handleFileChange} />
      </form>
      {uploadedFiles.length > 0 && (
        <div className="mt-4">
          <h3 className="text-lg font-semibold text-gray-700">Uploaded Documents</h3>
          <table className="w-full border-collapse border border-gray-300 mt-2">
            <thead>
              <tr className="bg-gray-100">
                <th className="border p-2">DOCUMENT NAME</th>
                <th className="border p-2">TYPE</th>
                <th className="border p-2">FILE SIZE</th>
                <th className="border p-2">DATE UPLOADED</th>
                <th className="border p-2">ACTIONS</th>
              </tr>
            </thead>
            <tbody>
  {uploadedFiles.flatMap((file) =>
    file.fileNames.map((fileName, index) => (
      <tr key={`${file.id}-${index}`} className="text-center">
        <td className="border p-2">{fileName}</td>
        <td className="border p-2">{file.fileTypes[index]}</td>
        <td className="border p-2">{file.fileSizes[index]}</td>
        <td className="border p-2">{file.uploadedAt}</td>
        <td className="border p-2 space-x-2">
          <button className="text-blue-600" onClick={() => window.open(`http://localhost:8043/api/documents/view/${file.id}/${index}`)}>
            <FaEye />
          </button>
          <button className="text-green-600" onClick={() => window.open(`http://localhost:8043/api/documents/download/${file.id}/${index}`)}>
            <FaDownload />
          </button>
          <button className="text-red-600" onClick={() => handleDelete(file.id, index)}>
  <MdDelete />
</button>

        </td>
      </tr>
    ))
  )}
</tbody>

          </table>
        </div>
      )}
    </div>
  );
}

