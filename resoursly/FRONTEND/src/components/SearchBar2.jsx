
import { AiOutlineSearch } from 'react-icons/ai'
import { FiSearch } from "react-icons/fi";
const SearchBar2 = ({ onChange }) => {
    const handleKeyDown = (event) => {
      if (event.key === 'Enter') {
        event.preventDefault();
      }
    };
  
    return (
      <form className="relative flex items-center w-[300px] mt-[22px]">
        <input
          type="search"
          onChange={(e) => onChange(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Search Profiles..."
          className="w-full h-[40px] px-4 pl-10 rounded-md border-2 border-[#0B6EFD] focus:bg-white focus:ring-2 focus:ring-blue-500"
        />
        <FiSearch className="absolute left-3 text-gray-500" />
      </form>
    );
  };
  
  export default SearchBar2;
  