// import React from 'react';
import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import SearchBar from "../components/SearchBar";
// import { data } from '../assets/data';
import "../components/SMEList.css";
import { Pagination, Stack } from "@mui/material";
import { FaRegEye } from "react-icons/fa"; // Consolidated import of FaRegEye from react-icons/fa
import { MdEdit } from "react-icons/md";
import { showProfile, viewOnlyProfile } from "../feature/PopupSlice";
import { setProfileId } from "../feature/CurrentProfile";
import searchClient from "../AxiosClient/SearchClient";
import { editProfileOff, editProfileOn } from "../feature/EditMode";
import { FaSpinner } from "react-icons/fa";
import axios from "axios";
import FilterBar from "./Filtter";

const stringToColor = (string) => {
  let hash = 0;
  for (let i = 0; i < string.length; i++) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }
  const color = `hsl(${hash % 360}, 70%, 70%)`;
  return color;
};

const getInitials = (name) => {
  const nameParts = name.split(" ");
  return nameParts.length > 1
    ? nameParts[0][0] + nameParts[1][0]
    : nameParts[0][0];
};

function SMEList() {
  const [search, setSearch] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [dataToLoad, setDataToLoad] = useState([]);
  const recordsPerPage = 10;
  const [colorMap, setColorMap] = useState({});
  const isViewOnly = useSelector((state) => state.popup.isReadOnly);
  const [defaultSearch, setDefaultSearch] = useState(undefined);
  const dispatch = useDispatch();
  var [loading, setLoading] = useState(true);

  const viewClickHandler = (profileId) => {
    console.log(`---VIEW CLICKED--- for ID : ${profileId}`);
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    dispatch(viewOnlyProfile());
  };

  const editClickHandler = (profileId) => {
    console.log(`---EDIT CLICKED--- for ID : ${profileId}`);
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    dispatch(editProfileOn());
  };

  const fetchApprovedProfiles = async (page = 1, searchParams) => {
    try {
      let url = `http://localhost:8090/search/profiles/approved?page=${
        page - 1
      }&size=10`;
      let searchQury = searchParams;
      if (defaultSearch && searchParams == undefined) {
        searchQury = defaultSearch;
      }
      // debugger;
      if (searchQury) {
        console.log(`search${searchQury}`);
        setDefaultSearch(searchQury);
        const queryParams = [];

        if (searchQury.location) {
          console.log("hello");
        }

        if (searchQury.roles && searchQury.roles !== "") {
          queryParams.push(`roleId=${searchQury.roles.id}`);
        }
        if (searchQury.vertical && searchQury.vertical !== "") {
          queryParams.push(`verticalId=${searchQury.vertical.id}`);
        }
        if (searchQury.country && searchQury.country !== "") {
          queryParams.push(`country=${searchQury.country.id}`);
        }

        const queryString = queryParams.join("&");
        console.log(queryString);
        url = `http://localhost:8090/search/profiles/approved?${queryString}&page=${
          page - 1
        }&size=10`;
        console.log(url);
      }
      setLoading(true);
      const res = await axios.get(url);

      setDataToLoad(res.data.content);
      setTotalPages(res.data.totalPages);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching approved profiles", error);
      setLoading(false);
    }
  };

  // testPage();

  // const fetchApprovedProfiles = async () => {
  //   const res = await searchClient
  //     .get(`/approved`)
  //     .then((response) => response)
  //     .catch((error) => {
  //       console.log(
  //         "There is some error while fetching approved profiles",
  //         error
  //       );
  //     });

  //   console.log("----APPROVED PROFILES-----");
  //   const sortedData = res.data.map(
  //     ({ profileStatus, feedbackRating, profileLifecycle, ...sortedData }) =>
  //       sortedData
  //   );
  //   console.log(sortedData);
  //   setDataToLoad(sortedData);
  //   setLoading(false);
  // };

  useEffect(() => {
    fetchApprovedProfiles(currentPage);
  }, [currentPage]);

  const capitalizeFirstLetter = (skill) => {
    if (!skill) return skill;
    else if (skill.length < 4) return skill.toUpperCase();
    return skill.charAt(0).toUpperCase() + skill.slice(1);
  };

  const filterData = (item) => {
    const lowerSearch = search.toLowerCase();

    // To search if the value in the search box matches any of the fields
    return (
      (item.name?.toLowerCase() || "").includes(lowerSearch) ||
      (item.primaryEmail?.toLowerCase() || "").includes(lowerSearch) ||
      (item.professionalExperience?.profession?.toLowerCase() || "").includes(
        lowerSearch
      ) ||
      (item.address?.toLowerCase() || "").includes(lowerSearch) ||
      (item.professionalExperience?.primaryDisciplines || []).some((skill) =>
        (skill?.toLowerCase() || "").includes(lowerSearch)
      ) ||
      Math.round(item.professionalExperience?.experienceYears || 0)
        .toString()
        .includes(lowerSearch)
    );
  };

  // Get filtered data
  const filteredData = dataToLoad.filter(filterData);

  // Pagination
  // const totalPages = Math.ceil(filteredData.length / recordsPerPage);
  const currentData = filteredData.slice(
    (currentPage - 1) * recordsPerPage,
    currentPage * recordsPerPage
  );

  const handlePageChange = (event, page) => {
    setCurrentPage(page);
  };

  return (
    <>
      <div className="relative overflow-x-auto sm:rounded-lg w-[1250px]">
        {/* <SearchBar onChange={setSearch} /> */}
        <FilterBar
          onSearchCallback={(e) => fetchApprovedProfiles(currentPage, e)}
        />
        {loading ? (
          <div className="flex justify-center items-center py-10">
            <FaSpinner className="animate-spin text-blue-500 text-3xl" />
          </div>
        ) : dataToLoad.length === 0 ? (
          <div className="text-center text-gray-500 text-lg mt-4 min-h-[400px]">
            No results found
          </div>
        ) : (
          <>
            <table className="w-[95%] ml-9 table-fixed text-sm text-left rtl:text-right">
              <thead className="bg-white text-gray-600 text-xs font-bold uppercase">
                <tr>
                  <th scope="col" className="px-4 py-3 w-[5%]"></th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[18%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Name
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[27%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Email
                  </th>
                  
                  <th
                    scope="col"
                    className="px-4 py-3 w-[22%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Role
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[22%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Vertical
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[15%] text-center overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Experience
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[22%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Location
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[36%] overflow-hidden whitespace-normal text-ellipsis"
                  >
                    Skills
                  </th>
                  <th
                    scope="col"
                    className="px-4 py-3 w-[18%] overflow-hidden whitespace-nowrap text-ellipsis"
                  >
                    Action
                  </th>
                </tr>
              </thead>
              <tbody>
                {dataToLoad.map((item) => (
                  <tr
                    key={item.id}
                    className="bg-[#FFFFFF] border-b dark:border-[#0000001A] shadow-sm text-[#212020] text-[14px]"
                  >
                    <td className="px-4 py-3">
                      <div
                        className="w-6 h-6 rounded-full flex items-center justify-center text-xs text-white"
                        style={{
                          fontSize: "8px",
                          backgroundColor: stringToColor(item.name),
                        }}
                      >
                        {getInitials(item.name)}
                      </div>
                    </td>

                    <td className="px-4 py-3 overflow-hidden whitespace-nowrap text-ellipsis">
                      {item.name}
                    </td>
                    <td className="px-4 py-3 overflow-hidden whitespace-nowrap text-ellipsis">
                      {item.primaryEmail}
                    </td>
                    
                    <td className="px-4 py-3 overflow-hidden whitespace-nowrap text-ellipsis">
                      {item.role[0].roleName}
                    </td>
                    <td className="px-4 py-3 overflow-hidden whitespace-nowrap text-ellipsis">
                      {item.vertical[0].verticalName}
                    </td>
                    <td className="px-4 py-3 overflow-hidden text-center whitespace-nowrap text-ellipsis">
                      {Math.round(item.professionalExperience.experienceYears)}
                    </td>
                    <td className="px-4 py-3 overflow-hidden whitespace-nowrap text-ellipsis">
                      {item.country}
                    </td>
                    <td className="px-4 py-3 overflow-hidden whitespace-normal text-ellipsis">
                      {item.professionalExperience.primaryDisciplines
                        .map(capitalizeFirstLetter)
                        .slice(0, 3)
                        .join(", ")}
                    </td>
                    <td className="px-4 py-2 flex gap-3 items-center">
                      <FaRegEye onClick={() => viewClickHandler(item.id)} />
                      <MdEdit onClick={() => editClickHandler(item.id)} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination Controls */}
            {dataToLoad.length && (
              <Stack spacing={2} direction="row" justifyContent="center" mt={2}>
                <Pagination
                  count={totalPages}
                  page={currentPage}
                  onChange={handlePageChange}
                  shape="rounded"
                  variant="outlined"
                  sx={{
                    "& .MuiPaginationItem-root": {
                      border: "1px solid #1976d2",
                      color: "#1976d2",
                      borderRadius: "8px",
                      "&.Mui-selected": {
                        backgroundColor: "#1976d2",
                        color: "#fff",
                      },
                      "&:hover": {
                        backgroundColor: "rgba(25, 118, 210, 0.1)",
                      },
                    },
                  }}
                />
              </Stack>
            )}
          </>
        )}
      </div>
    </>
  );
}

export default SMEList;
