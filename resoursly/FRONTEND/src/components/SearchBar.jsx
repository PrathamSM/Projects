import { FiSearch } from "react-icons/fi";
 
const SearchBar = ({ onChange }) => {
    const handleKeyDown = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
        }
    };
 
    return (
        <form className="w-[400px] h-[60px] relative ml-[0px] overflow-visible">  {/* Moved to left */}
            <div className="relative">
                <input
                    type="search"
                    onChange={(e) => onChange(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Search"
                    className="w-80 hover:bg-[#E8EDF0] focus:bg-[#FFFFFF] p-2 pl-[40px] rounded-full
                    text-[14px] bg-[#FFFFFF] border-[#0B6EFD] border-2 focus:outline-none"
                />
                <FiSearch className="absolute top-1/2 left-4 transform -translate-y-1/2 text-gray-500" />
            </div>
        </form>
    );
};
 
export default SearchBar;