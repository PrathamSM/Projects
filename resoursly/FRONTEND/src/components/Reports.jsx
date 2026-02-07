import React, { useEffect, useState } from "react";
import { FaRegStar, FaStar } from "react-icons/fa";
import { PieChart, Pie, Cell, Tooltip, Legend } from "recharts";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const DataTable = ({ data }) => {
  const [searchTerm, setSearchTerm] = useState("");

  // Filter data based on location or roles
  const filteredData = (data || []).filter(
    (row) =>
      row.location?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      row.roles?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="bg-white shadow-md rounded-lg p-4 col-span-2 bg-white shadow-md rounded-lg p-4">
      {/* Search Bar - Figma Styled */}
      <div className="flex justify-between items-center mb-3">
        <div className="w-[300px] h-8 pl-3 pr-[93px] py-1.5 bg-white rounded border border-[#d9e2e9] flex items-center overflow-hidden">
          {/* Search Icon */}
          <svg
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className="text-[#A0B2BD]"
          >
            <g clipPath="url(#clip0_165_2641)">
              <path
                d="M10.8 10.8004L14.6 14.6004M12.2 6.80039C12.2 9.78273 9.78236 12.2004 6.80002 12.2004C3.81769 12.2004 1.40002 9.78273 1.40002 6.80039C1.40002 3.81805 3.81769 1.40039 6.80002 1.40039C9.78236 1.40039 12.2 3.81805 12.2 6.80039Z"
                stroke="currentColor"
                strokeWidth="1.2"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </g>
            <defs>
              <clipPath id="clip0_165_2641">
                <rect width="16" height="16" fill="white" />
              </clipPath>
            </defs>
          </svg>

          {/* Search Input */}
          <input
            type="text"
            placeholder="Search by location, roles, etc"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="text-[#6b818f] text-xs font-normal font-roboto leading-tight bg-transparent focus:outline-none ml-2 w-full"
          />
        </div>
      </div>

      {/* Table Container */}
      <div
        className="overflow-y-auto max-h-[455px] border rounded-lg 
                scrollbar scrollbar-thumb-gray-400 scrollbar-track-gray-200 
                scrollbar-thin scrollbar-thumb-gray-500 scrollbar-track-gray-300"
      >
        <table className="w-full border-collapse">
          {/* Table Header */}
          <thead className="bg-[#d9e2e9] sticky top-0 z-10">
            <tr className="text-[#0e3249] text-xs font-roboto leading-[18px]">
              <th className="h-11 px-6 py-3 border-b border-[#d9e2e9] text-left font-semibold">
                Location
              </th>
              <th className="h-11 px-6 py-3 border-b border-[#d9e2e9] text-left font-semibold">
                No. of Resources
              </th>
              <th className="h-11 px-6 py-3 border-b border-[#d9e2e9] text-left font-semibold">
                Roles
              </th>
            </tr>
          </thead>

          {/* Table Body */}
          <tbody>
            {filteredData.length > 0 ? (
              filteredData.map((row, index) => (
                <tr key={index} className="border-b border-[#d9e2e9]">
                  {/* Location Cell */}
                  <td className="h-[52px] px-6 py-4 text-[#0e3249] text-sm font-medium font-roboto leading-tight">
                    {row.location}
                  </td>

                  {/* No. of Resources Cell */}
                  <td className="h-[52px] px-6 py-4 text-[#0f3349] text-sm font-normal font-roboto leading-[20px]">
                    {row.resources}
                  </td>

                  {/* Roles Cell */}
                  <td className="h-[52px] px-6 py-4 text-[#0f3349] text-sm font-normal font-roboto leading-[20px]">
                    {row.roles}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="3" className="p-2 text-center text-gray-500">
                  No matching data found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];

const PieChartCard = ({ title, data }) => {
  return (
    <div className="bg-white shadow-md rounded-lg p-3 border flex flex-col items-center">
      {/* Title */}
      <h2 className="w-full max-w-[342px] text-[#0e3249] text-base font-semibold font-roboto mb-2 self-start">
        {title}
      </h2>
      {/* Pie Chart */}
      <PieChart width={200} height={150}>
        <Pie
          data={data}
          cx="50%"
          cy="45%"
          outerRadius={65}
          innerRadius={35}
          fill="#8884d8"
          dataKey="value"
        >
          {data.map((_, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
      </PieChart>

      {/* Legend Section */}
      <div className="w-full mt-1">
        {data.map((entry, index) => (
          <div
            key={index}
            className="flex justify-between items-center px-2 text-sm text-gray-600"
          >
            {/* Entry Name - Styled as per Figma */}
            <div className="flex items-center">
              <div
                className="w-2.5 h-2.5 rounded-full mr-1"
                style={{ backgroundColor: COLORS[index] }}
              ></div>
              <span className="text-[#0e3249] text-sm font-medium font-roboto">
                {entry.name}
              </span>
            </div>

            {/* Entry Value - Styled as per Figma */}
            <div className="h-4 rounded-lg flex flex-col justify-center items-start">
              <div className="self-stretch text-black text-sm font-normal font-roboto leading-none">
                {entry.value}%
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

const StatCard = ({ title, value, isRating }) => {
  const navigate = useNavigate();

  const handleClick = (e) => {
    if(title==="Total Resources"){

      navigate(`/sme`, { state:  "dashboard" }); // replace '/another-page' with your target route
    }else if(title === "Total Projects"){

      navigate(`/create-project`, { state: {name: "dashboard"} });
    }
  };
  return (
    <div onClick={(e)=>{e.preventDefault(),handleClick(e)}} className="bg-white shadow-md rounded-lg p-6 flex flex-col items-start cursor-pointer hover:bg-gray-200 transition-transform duration-150 active:scale-95">
      <p className="text-[14px] font-normal text-[#0F3349] font-roboto">
        {title}
      </p>
      {isRating ? (
        <div className="flex items-center mt-2">
          {[...Array(5)].map((_, index) =>
            index < value ? (
              <FaStar key={index} className={`text-yellow-500 text-xl ${index !== 0 ? "m-1" : ""}`} />
            ) : (
              <FaRegStar key={index} className={`text-yellow-500 text-xl ${index !== 0 ? "m-1" : ""}`} />
            )
          )}
        </div>
      ) : (
        <h2 className="text-[33px] font-semibold text-[#0F3349] font-roboto">
          {value}
        </h2>
      )}
    </div>
  );
};

const Reports = () => {
  const [profilesCount, setProfilesCount] = useState(0);
  const [countryCount, setCountryCount] = useState(0);
  const [projectCount, setProjectCount] = useState(0);
  const [tableData, setTableData] = useState([]);
  const [countryData, setCountryData] = useState([]);

  const fetchProfilesCount = async () => {
    let cnt = await axios.get(`http://localhost:8085/profile-status/total`);
    // setProfilesCount(cnt?.data?.profileCount);
    setProfilesCount(cnt?.data);
  }

  const fetchCountryCount = async () => {
    let ccnt = await axios.get(`http://localhost:8081/profiles/country-count`);
    setCountryCount(ccnt?.data?.countryCount);
  }

  const fetchProjectCount = async () => {
    let pcnt = await axios.get(`http://localhost:8040/projects/total-count`);
    setProjectCount(pcnt?.data?.projectsCount);
  }

  const fetchTableData = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/profiles/grouped-by-country`); 
      const apiData = response?.data?.groupedData;
  
      // Add roles based on location
      const formattedData = apiData.map(item => ({
        ...item,
        roles: locationRolesMapping[item.location] || "SME, Language Experts, QA, ID"
      }));
  
      setTableData(formattedData);
    } catch (error) {
      console.error("Error fetching table data:", error);
    }
  };

  const getCountryData = (tableData) => {
    const slicedData = tableData.slice(0, 5);
    const totalProfiles = tableData.length;
    const totalResources = slicedData.reduce((sum, item) => sum + item.resources, 0);
  
    return slicedData.map((item) => ({
      name: item.location,
      flag: `https://flagcdn.com/w40/${countryFlagMap[item.location] || "un"}.png`,
      percentage: totalResources > 0 ? ((item.resources / totalResources) * 100).toFixed(2) : "0.00",
    }));
  };

  useEffect(() => {
    fetchProfilesCount();
    fetchCountryCount();
    fetchProjectCount();
    fetchTableData();
  }, []);

  useEffect(() => {
    const cData = getCountryData(tableData);
    setCountryData(cData);
  }, [tableData])

  const stats = [
    { title: "Total Resources", value: profilesCount },
    { title: "Total Countries Covered", value: countryCount },
    { title: "Total Projects", value: projectCount },
    { title: "Average Resource Rating", value: 4, isRating: true },
  ];

  // const tableData = [
  //   { location: "USA", resources: "7,500+", roles: "SME, ID, PM, Back office, Technology" },
  //   { location: "India", resources: "3,000+", roles: "SME, PM, Technology, ID, Designer" },
  //   { location: "Canada", resources: "1,000", roles: "LMS Admin, UX/UI Designer, ID, SME" },
  //   { location: "UK", resources: "500", roles: "QA, ID, Language Experts" },
  //   { location: "Germany", resources: "400", roles: "E-learning Specialist, QA, Technology, PM" },
  //   { location: "Australia", resources: "300", roles: "PM, SME, Data Analyst, Technical Writer" },
  //   { location: "France", resources: "250", roles: "ID, Back office, Designer" },
  //   { location: "Japan", resources: "200", roles: "SME, Designer, Content Strategist, Customer Success Manager" },
  //   { location: "Singapore", resources: "150", roles: "Curriculum Developer, Technical Writer, Researcher" },
  //   { location: "Netherlands", resources: "150", roles: "LMS Admin, UX/UI Designer, ID, SME" },
  //   { location: "Italy", resources: "120", roles: "Content Strategist, QA, ID" },
  //   { location: "Spain", resources: "100", roles: "Video Producer" },
  //   { location: "Brazil", resources: "90", roles: "Sales Coordinator" },
  //   { location: "Mexico", resources: "80", roles: "Training Coordinator" },
  //   { location: "South Africa", resources: "70", roles: "Technical Writer" },
  //   { location: "UAE", resources: "60", roles: "Corporate Trainer" },
  //   { location: "Korea", resources: "50", roles: "Instructional Designer" },
  // ];

  const pieChartData = [
    { name: "SME", value: 55 },
    { name: "PM", value: 15 },
    { name: "Language Experts", value: 10 },
    { name: "Others", value: 20 },
  ];

  // const countryData = [
  //   {
  //     name: "United States",
  //     percentage: 53,
  //     flag: "https://flagcdn.com/w40/us.png",
  //   },
  //   { name: "India", percentage: 21, flag: "https://flagcdn.com/w40/in.png" },
  //   { name: "Canada", percentage: 7, flag: "https://flagcdn.com/w40/ca.png" },
  //   { name: "Canada", percentage: 7, flag: "https://flagcdn.com/w40/ca.png" },
  //   { name: "Canada", percentage: 7, flag: "https://flagcdn.com/w40/ca.png" },
  // ];
  const locationRolesMapping = {
    "USA": "SME, ID, PM, Back office, Technology, Language Experts",
    "India": "SME, PM, Technology, ID, Designer",
    "Canada": "LMS Admin, UX/UI Designer, ID, SME",
    "UK": "QA, ID, Language Experts, Sales Coordinator, Corporate Trainer",
    "Germany": "E-learning Specialist, QA, Technology, PM",
    "Australia": "PM, SME, Data Analyst, Technical Writer",
    "France": "ID, Back office, Designer",
    "Japan": "SME, Designer, Content Strategist, Customer Success Manager",
    "Singapore": "Curriculum Developer, Technical Writer, Researcher, ID, PM",
    "Netherlands": "LMS Admin, UX/UI Designer, ID, SME",
    "Italy": "Content Strategist, SME, QA, ID, Technology",
    "Spain": "Video Producer",
    "Brazil": "Sales Coordinator",
    "Mexico": "Training Coordinator",
    "South Africa": "Technical Writer, ID, Language Experts, PM",
    "UAE": "Corporate Trainer",
    "Korea": "Technology",
    "Hong Kong" : "Curriculum Developer, Technical Writer",
    "Hongkong" : "Curriculum Developer, Technical Writer, Sales Coordinator",
    "Indonesia" : "SME, Curriculum Developer, Technical Writer, Sales Coordinator",
    "Phillipines" : "Curriculum Developer",
    "Poland" : "ID",
    "Sri Lanka" : "Corporate Trainer",
    "Lithuania" : "SME",
    "Dubai" : "Data Analyst",
    "Belgium" : "PM, Technical Writer",
    "Bangladesh" : "ID, Sales Coordinator",
    "Romania" : "SME, QA",
    "Finland" : "Sales Coordinator, QA",
    "Portugal" : "SME, Language Experts, ID",
  };
  const countryFlagMap = {
    "United States": "us",
    "USA": "us",
    "India": "in",
    "Canada": "ca",
    "United Kingdom": "gb",
    "Germany": "de",
    "Australia": "au",
    "France": "fr",
    "Japan": "jp",
    "Singapore": "sg",
    "Netherlands": "nl",
    "Italy": "it",
    "Spain": "es",
    "Brazil": "br",
    "Mexico": "mx",
    "South Africa": "za",
    "United Arab Emirates": "ae",
    "South Korea": "kr",
    "Switzerland": "ch",
    "UK": "gb"
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen flex flex-col gap-6">
      {/* Stat Cards */}
      <div className="grid grid-cols-4 gap-4">
        {stats.map((stat, index) => (
          <StatCard
            key={index}
            title={stat.title}
            value={stat.value}
            isRating={stat.isRating}
          />
        ))}
      </div>

      {/* Main Content */}
      <div className="grid grid-cols-3 gap-4">
        {/* Table Section */}
        {/* <div className="col-span-2 bg-white shadow-md rounded-lg p-4"> */}
        <DataTable data={tableData} />
        {/* </div> */}

        {/* Charts Section */}
        <div className="flex flex-col gap-4">
          <PieChartCard
            title="Resource Allocation by Roles"
            data={pieChartData}
          />
          <div className="bg-white shadow-md rounded-lg p-4">
            {/* Title */}
            <h2 className="text-[#0e3249] text-base font-semibold font-roboto mb-[16px]">
              Resources By Country
            </h2>

            {/* Total Count */}
            <p className="text-[#4f626e] text-sm font-normal font-roboto">
              Total
            </p>
            <h2 className="text-[#0e3249] text-2xl font-bold font-roboto">
              {profilesCount}  
            </h2>
            {/* Country List */}
            <div className="mt-3">
              {countryData.map((country, index) => (
                <div
                  key={index}
                  className="flex justify-between items-center mb-2"
                >
                  {/* Flag & Country Name */}
                  <div className="flex items-center">
                    <img
                      src={country.flag}
                      alt={country.name}
                      className="w-6 h-6 rounded-full mr-2"
                    />
                    <div className="w-[266px] text-[#0e3249] text-sm font-medium font-roboto">
                      {country.name}
                    </div>
                  </div>
                  {/* Percentage */}
                  <div className="text-right text-[#4f626e] text-sm font-normal font-roboto">
                    {country.percentage}%
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Reports;
