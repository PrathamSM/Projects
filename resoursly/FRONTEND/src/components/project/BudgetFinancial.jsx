import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import React, { useState, useEffect } from "react";
import { RiMoneyDollarBoxLine } from "react-icons/ri";

const BudgetFinancial = ({ projectId, onSave }) => {

    // used this to check that the project Id is comming in this component correctly.
  console.log("Received projectId in BudgetFinancial:", projectId);

  const [formData, setFormData] = useState({
    currency: "INR",
    plannedBudget: "",
    billingModel: "Time & Material",
  });

  useEffect(() => {
    console.log("Updated projectId in BudgetFinancial:", projectId);
  }, [projectId]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Validate Form Data
const validate = () => {
    const errors = {};
    if (!formData.currency) errors.currency = "Currency is required.";
    if (!formData.plannedBudget) errors.plannedBudget = "Planned Budget is required.";
    if (!formData.billingModel) errors.billingModel = "Billing Model is required.";

    if (Object.keys(errors).length > 0) {
      Object.values(errors).forEach((err) => toast.error(err));
      return false;
    }
    return true;
  };
  

  // Function to handle PUT request
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevent page reload

    if (!validate()) return; //validating all the fields.

    if (!projectId) {
      toast.error("Project ID is missing!");
      return;
    }

    try {
      const response = await axios.put(
        `http://localhost:8040/projects/${projectId}`,
        {
          ...formData,
        },
        {
          headers: {
            "Content-Type": "application/json", // Ensure proper request format
          },
        }
      );

      console.log("PUT response:", response.data); // i used this to see the submitted data
      toast.success("Budget and Financial data updated successfully!");
      onSave(); // use to save the form data and Move to the next step
    } catch (error) {
      console.error("Error updating Budget & Financial:", error.response?.data || error.message);
      toast.error("Failed to update Budget & Financial.");
    }
  };

  return (
    <div className="p-8 bg-white shadow-md rounded-lg">
      {/* Header */}
      <h3 className="text-xl font-semibold text-blue-500 mb-6 flex items-center gap-2">
        <RiMoneyDollarBoxLine />
        Budget and Financial
      </h3>
        {/* Billing Model */}
        <div className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700">Billing Model</h2>
          <h3 className="text-sm text-gray-600 mb-3">
            Choose a billing model that suits your project needs.
          </h3>
          <p className="text-xs text-gray-500 mb-1"> <strong>**Fixed</strong>: Pre-agreed amount for the project.</p>
          <p className="text-xs text-gray-500 mb-1"> <strong>**Time & Material</strong>: Based on actual time and resources used.</p>
        

          <div className="mt-4 flex flex-wrap gap-6">
            {["Fixed", "Time & Material"].map((model) => (
              <label key={model} className="flex items-center gap-3 text-gray-700">
                <input
                  type="radio"
                  name="billingModel"
                  value={model}
                  checked={formData.billingModel === model}
                  onChange={handleChange}
                  className="accent-blue-500"
                />
                {model}
              </label>
            ))}
          </div>
        </div>

      {/* Currency and Budget */}
      <form id="budgetFinancialForm" onSubmit={handleSubmit} className="space-y-8">
        <div className="grid grid-cols-3 gap-8">
          <div>
            <label className="block text-sm font-medium text-gray-700">Currency</label>
            <select
              name="currency"
              value={formData.currency}
              onChange={handleChange}
              className="mt-2 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition p-2"
            >
              <option value="USD">USD</option>
              <option value="EUR">EUR</option>
              <option value="INR">INR</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">cost*</label>
            <input
              type="number"
              name="plannedBudget"
              value={formData.plannedBudget}
              onChange={handleChange}
              className="mt-2 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition p-2"
            />
          </div>
            
          {formData.billingModel === "Time & Material" && (
          <div>
            <label className="block text-sm font-medium text-gray-700">Billing Rate</label>
            <select
              name="currency"
              value={formData.currency}
              onChange={handleChange}
              className="mt-2 block w-full border-b-2 border-black focus:border-blue-500 hover:border-blue-500 outline-none transition p-2"
            >
              <option value="USD"><h5>Monthly</h5></option>
              <option value="EUR"><h5>Hourly</h5></option>
             
            </select>
          </div>
          )}
        </div>

       
        <ToastContainer />
      </form>
    </div>
  );
};

export default BudgetFinancial;
