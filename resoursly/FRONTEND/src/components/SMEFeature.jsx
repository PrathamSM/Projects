import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import SearchBar from './SearchBar';
import '../components/SMEList.css';
import { FaRegEye } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import { showProfile, viewOnlyProfile } from '../feature/PopupSlice';
import { setProfileId } from '../feature/CurrentProfile';
import searchClient from '../AxiosClient/SearchClient';
//import '../components/SMEFeatures.css';
const stringToColor = (string) => {
    let hash = 0;
    for (let i = 0; i < string.length; i++) {
        hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }
    const color = `hsl(${hash % 360}, 70%, 70%)`;
    return color;
};

const getInitials = (name) => {
    const nameParts = name.split(' ');
    return nameParts.length > 1
        ? nameParts[0][0] + nameParts[1][0]
        : nameParts[0][0];
};

function SMEFeature() {
    const [search, setSearch] = useState('');
    const [datatoLoad, setDataToLoad] = useState([]);
    const [showAll, setShowAll] = useState(false); // State to toggle "Show more"
    const dispatch = useDispatch();

    const viewClickHandler = (profileId) => {
        dispatch(setProfileId(profileId));
        dispatch(showProfile());
        dispatch(viewOnlyProfile());
    };

    const editClickHandler = (profileId) => {
        dispatch(setProfileId(profileId));
        dispatch(showProfile());
    };

    const fetchApprovedProfiles = async () => {
        try {
            const res = await searchClient.get();
            const sortedData = res.data.map(({ profileStatus, feedbackRating, profileLifecycle, ...otherData }) => ({
                ...otherData,
                profileStatus,
            }));
            setDataToLoad(sortedData);
        } catch (error) {
            console.error('Error fetching profiles:', error);
        }
    };

    useEffect(() => {
        fetchApprovedProfiles();
    }, []);

    const filterData = (item) => {
        const lowerSearch = search.toLowerCase();
        return (
            (item.name?.toLowerCase() || '').includes(lowerSearch) ||
            (item.primaryEmail?.toLowerCase() || '').includes(lowerSearch) ||
            (item.professionalExperience?.profession?.toLowerCase() || '').includes(lowerSearch) ||
            (item.adress?.toLowerCase() || '').includes(lowerSearch) ||
            (item.professionalExperience?.primaryDisciplines || [])
                .some((skill) => (skill?.toLowerCase() || '').includes(lowerSearch)) ||
            (Math.round(item.professionalExperience?.experienceYears || 0))
                .toString()
                .includes(lowerSearch)
        );
    };

    const filteredData = datatoLoad.filter(filterData);

    // Determine the data to display based on "showAll" state
    const dataToShow = showAll ? filteredData : filteredData.slice(0, 3);

    return (
        <>
            <div className="relative overflow-x-auto sm:rounded-lg w-[1250px]">
                <SearchBar onChange={setSearch} />

                {filteredData.length === 0 ? (
                    <div className="text-center text-gray-500 text-lg mt-4">No results found</div>
                ) : (
                    <>
                        <table className="w-[95%] ml-9 table-fixed text-sm text-left rtl:text-right">
                            <thead className="bg-white text-gray-600 text-xs font-bold uppercase">
                                <tr>
                                    <th className="px-4 py-3 w-[5%]"></th>
                                    <th className="px-4 py-3 w-[18%]">Name</th>
                                    <th className="px-4 py-3 w-[27%]">Email</th>
                                    <th className="px-4 py-3 w-[22%]">Designation</th>
                                    <th className="px-4 py-3 w-[15%] text-center">Experience</th>
                                    <th className="px-4 py-3 w-[22%]">Location</th>
                                    <th className="px-4 py-3 w-[36%]">Skills</th>
                                    <th className="px-4 py-3 w-[18%]">Approval</th>
                                    <th className="px-4 py-3 w-[18%]">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {dataToShow.map((item) => (
                                    <tr
                                        key={item.id}
                                        className="bg-[#FFFFFF] border-b dark:border-[#0000001A] shadow-sm text-[#212020] text-[14px]"
                                    >
                                        <td className="px-4 py-3">
                                            <div
                                                className="w-6 h-6 rounded-full flex items-center justify-center text-xs text-white"
                                                style={{
                                                    fontSize: '8px',
                                                    backgroundColor: stringToColor(item.name),
                                                }}
                                            >
                                                {getInitials(item.name)}
                                            </div>
                                        </td>
                                        <td className="px-4 py-3">{item.name}</td>
                                        <td className="px-4 py-3 overflow-hidden text-ellipsis whitespace-nowrap">{item.primaryEmail}</td>
                                        <td className="px-4 py-3">{item.professionalExperience?.profession}</td>
                                        <td className="px-4 py-3 text-center">
                                            {Math.round(item.professionalExperience?.experienceYears || 0)}
                                        </td>
                                        <td className="px-4 py-3">{item.address}</td>
                                        <td className="px-4 py-3">
                                            {item.professionalExperience?.primaryDisciplines.slice(0, 3).join(', ')}
                                            {item.professionalExperience?.primaryDisciplines.length > 3 && (
                                                <span
                                                    style={{ color: 'blue', cursor: 'pointer' }}
                                                    onClick={() => alert('Show all skills!')}
                                                >
                                                    {' '}
                                                    Show more
                                                </span>
                                            )}
                                        </td>
                                        <td className="px-4 py-3">
                                            <span
                                                className={`px-2 py-1 rounded text-white ${
                                                    item.profileStatus?.isApproved ? 'bg-green-500' : 'bg-red-500'
                                                }`}
                                            >
                                                {item.profileStatus?.isApproved ? 'Approved' : 'Pending'}
                                            </span>
                                        </td>
                                        <td className="px-4 py-2 flex gap-3 items-center">
                                            <FaRegEye onClick={() => viewClickHandler(item.id)} />
                                            <MdEdit onClick={() => editClickHandler(item.id)} />
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {/* Show more button */}
                        <div className="text-center mt-4">
                            <button
                                onClick={() => setShowAll(!showAll)}
                                className="text-blue-500 hover:underline"
                            >
                                {showAll ? 'Show less' : 'Show more'}
                            </button>
                        </div>
                    </>
                )}
            </div>
        </>
    );
}

export default SMEFeature;
