import axios from "axios";
import React, { useEffect, useState } from "react";
import { FaRegStar, FaStar } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { PieChart, Pie, Cell, Tooltip, Legend } from "recharts";
// import { searchClient } from '../AxiosClient/SearchClient';
import profileStatusClient from "../AxiosClient/profileStatusClient";



const DataTable = ({ tableData }) => {
  const [searchTerm, setSearchTerm] = useState("");

  // Filtered Data - Matches Location or Roles
  const filteredData = tableData.filter(
    (row) => row.projectId.toLowerCase().includes(searchTerm.toLowerCase())
    // ||
    //   row.roles.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const navigate = useNavigate();

  const handleClick = (projectId) => {
   
      navigate(`/create-project`, { state:  {name: "dashboard", projectId: projectId},  });
  };
  return (
    <div className="bg-white shadow-md rounded-lg p-4 pt-2">
      {/* Search Bar */}
      <div
      className="m-2"
        style={{
          width: 342,
          color: "#0F3349",
          fontSize: 16,
          fontFamily: "Roboto",
          fontWeight: "500",
          wordWrap: "break-word",
        }}
      >
        Open Projects
      </div>
      
      {/* Table Container */}
      <div className="overflow-y-auto max-h-[400px] border rounded-lg">
        <table className="w-full border-collapse">
          <thead className="bg-[#D9E3E9] sticky top-0 z-10">
            <tr className="text-[#0F3349] text-left">
              <th className="p-2 border-b">Project ID</th>
              <th className="p-2 border-b">Project Name</th>
              <th className="p-2 border-b">Description</th>
              <th className="p-2 border-b">Status</th>
            </tr>
          </thead>
          <tbody>
            {filteredData.length > 0 ? (
              filteredData.map((row, index) => (
                <tr key={index} className="border-b hover:bg-gray-100 cursor-pointer" onClick={(e)=>{e.preventDefault(),handleClick(row.projectId)}}>
                  <td className="p-2">{row.projectId}</td>
                  <td className="p-2">{row.projectName}</td>
                  <td className="p-2">{row.projectDescription}</td>
                  <td className="p-2 flex  align-middle content-center"><span className="w-[12px] h-[12px] bg-red-700 rounded-full flex mt-[5px] mr-[3px]"></span>{row?.roles ? row?.roles : "OPEN"}</td>
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
      </div>{" "}
      <br />
      {/* <div
        style={{
          width: 342,
          color: "#0F3349",
          fontSize: 16,
          fontFamily: "Roboto",
          fontWeight: "500",
          wordWrap: "break-word",
        }}
      >
        Required Resources
      </div> */}
      {/* <div className="overflow-y-auto max-h-[400px] border rounded-lg">
        <table className="w-full border-collapse">
          <thead className="bg-gray-100 sticky top-0 z-10">
            <tr className="text-gray-600 text-left">
              <th className="p-2 border-b">Project Name</th>
              <th className="p-2 border-b">Resource Required</th>
              <th className="p-2 border-b">Requirement By</th>
            </tr>
          </thead>
          <tbody>
            {filteredData.length > 0 ? (
              filteredData.map((row, index) => (
                <tr key={index} className="border-b">
                  <td className="p-2">{row.resources}</td>
                  <td className="p-2">5 - Content Developer, 1 -	
                  Quality Assurance</td>
                  <td className="p-2">Mgrigank Bhusan</td>
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
      </div> */}
    </div>
  );
};

const COLORS = ["#D80303", "#14B8A6", "#FFBB28", "#FF8042"];

const PieChartCard = ({ title, data }) => {
  return (
    <div className="bg-white shadow-md rounded-lg p-3 border border-purple-300 flex flex-col items-center">
      <h2 className="text-gray-700 font-semibold text-md mb-2 self-start">
        {title}
      </h2>

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

      <div className="w-full mt-1">
        {data.map((entry, index) => (
          <div
            key={index}
            className="flex justify-between items-center px-2 text-sm text-gray-600"
          >
            <div className="flex items-center">
              <div
                className="w-2.5 h-2.5 rounded-full mr-1"
                style={{ backgroundColor: COLORS[index] }}
              ></div>
              <span>{entry.name}</span>
            </div>
            <span className="font-medium">{entry.value}%</span>
          </div>
        ))}
      </div>
    </div>
  );
};

const StatCard = ({ title, value, isRating, id }) => {
  const navigate = useNavigate();

  const handleClick = (e) => {
    console.log(title, title === "Resources Allocated")
    if(id == 1){

      navigate(`/sme`, { state:  "dashboard" }); // replace '/another-page' with your target route
    }else if(id == 2){

      navigate(`/create-project`, { state: {name: "dashboard"} });
    }
  };
  return (
    <div onClick={(e)=>{e.preventDefault(),handleClick(e)}} className="bg-white shadow-md rounded-lg p-6 flex flex-col items-start cursor-pointer hover:bg-gray-200 transition-transform duration-150 active:scale-95">
      <p className="text-[#0F3349] text-[14px] mb-[0px]" style={{fontWeight:'400',fontFamily: "Roboto",}}>{title}</p>
      {isRating ? (
        <div className="flex items-center mt-1">
          {[...Array(5)].map((_, index) =>
            index < value ? (
              <FaStar key={index} className="text-yellow-500 text-xl" />
            ) : (
              <FaRegStar key={index} className="text-yellow-500 text-xl" />
            )
          )}
        </div>
      ) : (
        <h2 style={{fontWeight:'600',fontFamily: "Roboto",}} className="text-[33px]  text-[#0F3349]">{value}</h2>
      )}
    </div>
  );
};

const Dashboard2 = () => {
  const [userEmail, setUserEmail] = useState("Kaushal");
  const [profilesCount, setProfilesCount] = useState(0);
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [ndaSignedCount, setNdaSignedCount] = useState(0);
  const [resourcesAllocated, setResourceAllocated] = useState(0);
  const [tableData, setTableData] = useState([]);
  const [countryData, setCountryData] = useState([]);

  // const [totalRes, setTotalRes]

  useEffect(() => {
    const mail = localStorage.getItem("userDisplayName");
    if (mail) {
      // setUserEmail("");
    }
  }, [userEmail]);

  useEffect(() => {
    axios
      .get("http://localhost:8040/projects")
      .then((response) => {
        setProjects(response.data);
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
      });
  }, []);

  const fetchApprovedProfilesCount = async () => {
    let a = await axios.get(`http://localhost:8085/profile-status/total`);
    setProfilesCount(a?.data);
  };
  const getResAllocated= async() =>{
    let res = await axios.get('http://localhost:8045/api/assignments/assigned/count');
    setResourceAllocated(res.data)
    }

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

    const fetchTableData = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/profiles/grouped-by-country`); 
        const apiData = response?.data?.groupedData;
    
        // Add roles based on location
        const formattedData = apiData.map(item => ({
          ...item,
          // roles: locationRolesMapping[item.location] || "SME, Language Experts, QA, ID"
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
    fetchApprovedProfilesCount();
    getResAllocated();
    fetchTableData();

  }, []);

  useEffect(() => {
      const cData = getCountryData(tableData);
      setCountryData(cData);
    }, [tableData])

  const fetchNdaSignedCount = async () => {
    try {
      const response = await profileStatusClient.get("/nda-signed-count");
      setNdaSignedCount(response.data); 
    } catch (error) {
      console.error("Error fetching NDA signed count:", error);
    }
  };

  useEffect(() => {
    fetchNdaSignedCount(); 
  }, []);


  const stats = [
    { title: "Total Resources", value: profilesCount, id:1 },
    { title: "Active Resources (NDA Signed)", value: ndaSignedCount, id:1 },
    { title: "Resources Allocated ", value: resourcesAllocated,id:1 },
    { title: "Resources Dedicated to Client", value: 84 ,id:3}, // Star rating
    { title: "Projects In-Progress", value: projects.length ,id:2},
  ];
  

  const pieChartData = [
    { name: "SME", value: 55 },
    { name: "PM", value: 15 },
    { name: "Language Experts", value: 10 },
    { name: "Others", value: 20 },
  ];



  return (
    <div className="p-6 bg-[#EFF4F6] min-h-screen flex flex-col gap-6">
      <div
      className="text-[#0F3349]"
        style={{
          color: "black",
          fontSize: 24,
          fontFamily: "Roboto",
          fontWeight: "700",
          wordWrap: "break-word",
        }}
      >
        Welcome back {userEmail}
      </div>
      <div
        style={{
          color: "#4F626E",
          fontSize: 16,
          fontFamily: "Roboto",
          fontWeight: "400",
          wordWrap: "break-word",
          marginTop: "-10px"
        }}
      >
        Here’s a quick overview of the platform’s status today."
      </div>

      {/* Stat Cards */}
      <div className="grid grid-cols-5 gap-4">
        {stats.map((stat, index) => (
          <StatCard
            key={index}
            title={stat.title}
            value={stat.value}
            isRating={stat.isRating}
            id={stat.id}
          />
        ))}
      </div>

      {/* Main Content */}
      <div className="grid grid-cols-3 gap-4">
        {/* Table Section */}
        <div className="col-span-2 bg-white shadow-md rounded-lg p-4">
          <DataTable tableData={projects}  />
        </div>

        {/* Charts Section */}
        <div className="flex flex-col gap-4">
          {/* new des start here  */}
<div className="flex flex-col gap-4">
<PieChartCard
            title="Resource Mix By Roles"
            data={pieChartData}
          />
          {/* <PieChartCard
            title="Resource Utilization"
            data={pieChartData}
          /> */}
          <div className="bg-white shadow-md rounded-lg p-4">
            
            <h2 className="text-[#0e3249] text-base font-semibold font-roboto mb-[16px]">
              Resources By Country
            </h2>

            <p className="text-[#4f626e] text-sm font-normal font-roboto">
              Total
            </p>
            <h2 className="text-[#0e3249] text-2xl font-bold font-roboto">
              {profilesCount}
            </h2>
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
          {/* new des end here  */}
          {/* <PieChartCard title="Project Status" data={pieChartData} />
          <div className="bg-white shadow-md rounded-lg p-3 border border-purple-300 flex flex-col items-center">
            
          <div className="overflow-y-auto max-h-[400px] border rounded-lg">
          <div
          className="my-1 mx-1"
        style={{
          width: 342,
          color: "#0F3349",
          fontSize: 16,
          fontFamily: "Roboto",
          fontWeight: "500",
          wordWrap: "break-word",
        }}
      >
        Required Resources
      </div>
            <table className="w-full border-collapse">
              <thead className="sticky top-0 z-10">
                <tr className="bg-[#D9E3E9] text-left text-[#0F3349]">
                  <th className="p-2 border-b text-nowrap">Project Name</th>
                  <th className="p-2 border-b text-nowrap">Resource Required</th>
                  <th className="p-2 border-b text-nowrap">Requirement By</th>
                </tr>
              </thead>
              <tbody>
                {projects.length > 0 ? (
                  projects.map((row, index) => (
                    <tr key={index} className="border-b">
                      <td className="p-2">{row.projectName}</td>
                      <td className="p-2">
                        5 - Content Developer, 1 - Quality Assurance
                      </td>
                      <td className="p-2">Mgrigank Bhusan</td>
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
        </div> */}
        </div>

        
      </div>
    </div>
  );
};

export default Dashboard2;
