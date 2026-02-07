import { useState, useEffect } from "react";
import axios from "axios";

const API_BASE_URL = "http://localhost:8084/feedbacks";

const InterviewFeedbackList = ({ profileId }) => {
  const [submittedFeedback, setSubmittedFeedback] = useState([]);

  useEffect(() => {
    if (profileId) {
      fetchFeedback(profileId);
    }
  }, [profileId]);

  const fetchFeedback = async (pId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/${pId}`);
      setSubmittedFeedback([response?.data]);
    } catch (error) {
      console.error("Error fetching feedback:", error);
    }
  };

  return (
    <div className="w-full mx-auto p-6 shadow-lg border border-gray-300 bg-white rounded-lg mt-6">
      <h3 className="text-lg font-semibold mb-2">Feedback History</h3>
      {submittedFeedback &&
        submittedFeedback.length > 0 &&
        submittedFeedback[0]?.interviewFeedbacks ? (
        submittedFeedback[0].interviewFeedbacks.map((feedback, index) => (
          <div
            key={index}
            className="relative p-6 mb-4 border rounded-lg shadow-md border-[#D9E3E9] bg-[#F7F9FB]"
          >
            {/* Grid Layout with Two Columns */}
            <div className="grid grid-cols-2 gap-4 items-center">
              <p className="font-semibold text-gray-800">Project Name:</p>
              <p className="font-normal text-gray-800">
                {feedback.name || "N/A"}
              </p>

              <p className="font-semibold text-gray-800">
                Client Name:
              </p>
              <p className="font-normal text-gray-800">
                {feedback.clientName || "N/A"}
              </p>

              <p className="font-semibold text-gray-800">Interview Level:</p>
              <p className="font-normal text-gray-800">
                {feedback.type || "N/A"}
              </p>

              <p className="font-semibold text-gray-800">
                Interview Taken By:
              </p>
              <div className="flex items-center space-x-2">
                <span>{feedback.interviewer || "N/A"}</span>
              </div>

              <p className="font-semibold text-gray-800">Interview Date:</p>
              <p className="font-normal text-gray-800">
                {feedback.date || "N/A"}
              </p>

              <p className="font-semibold text-gray-800">Result:</p>
              <p className="text-green-600 font-medium">
                {feedback.result || "N/A"}
              </p>

              {/* Display Overall Rating with Stars */}
              <p className="text-gray-600">
                Rating:
                {Array.from({ length: feedback.interviewRating }, (_, i) => (
                  <span key={i} className="text-yellow-500 text-xl mx-1">
                    ★
                  </span>
                ))}
              </p>

              <p className="font-semibold text-gray-800 col-span-2">
                Feedback:
              </p>
              <p className="text-gray-600 col-span-2">
                {feedback.feedbackComment || "N/A"}
              </p>
            </div>
          </div>
        ))
      ) : (
        <p className="text-gray-500">No feedback available.</p>
      )}
    </div>
  );
};

export default InterviewFeedbackList;

