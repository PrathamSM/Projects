import React, { useState, useRef, useEffect } from "react";
import { FiChevronDown, FiChevronUp } from "react-icons/fi";

function CustomDropdown({
  placeholder = "Select an option",
  options = [],
  selected,
  onSelect,
  keyname,
}) {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  // Close dropdown if user clicks outside
  useEffect(() => {
    function handleClickOutside(e) {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setIsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleToggle = () => setIsOpen(!isOpen);

  const handleOptionClick = (option) => {
    onSelect(option);
    setIsOpen(false);
  };

  return (
    <div className="relative inline-block" ref={dropdownRef}>
      {/* Pill-shaped button */}
      <button
        type="button"
        className="flex items-center appearance-none border border-gray-300 rounded-full 
                   px-4 py-2 text-gray-700 bg-white focus:outline-none 
                   focus:border-indigo-500 cursor-pointer"
        onClick={handleToggle}
      >
        <span>{selected[keyname] || placeholder}</span>
        <span className="ml-2">
          {isOpen ? <FiChevronUp /> : <FiChevronDown />}
        </span>
      </button>

      {isOpen && (
        <ul
          className="absolute left-0 mt-2 w-48 bg-white border border-gray-200 
                     rounded-md shadow-lg z-10 max-h-[250px] overflow-auto"
        >
          {options.map((opt) => (
            <li
              key={opt}
              className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
              onClick={() => handleOptionClick(opt)}
            >
              {opt[keyname]}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default CustomDropdown;
