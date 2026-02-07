import React, { useState, useMemo } from 'react';
import { Pagination, Stack } from '@mui/material';
import SearchBar2 from './SearchBar2';
import CityPieChart from '../assets/CityPieChart';  

const getValueByPath = (obj, path) => {
  return path.split('.').reduce((acc, key) => acc && acc[key], obj) || '';
};

const ReusableTable = ({ columns, data, recordsPerPage = 10, searchKeys = [] }) => {
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedCountry, setSelectedCountry] = useState('');
  const [selectedSkill, setSelectedSkill] = useState('');

  const countryOptions = [...new Set(data.map(item => item.location.split(', ').pop()))];
  const skillOptions = [...new Set(data.flatMap(item => item.primary_skills))];

  const filteredData = useMemo(() => {
    let tempData = [...data];

    if (selectedCountry) {
      if (currentPage > 1) setCurrentPage(1);
      tempData = tempData.filter(item => item.location.endsWith(selectedCountry));
    }

    if (selectedSkill) {
      if (currentPage > 1) setCurrentPage(1);
      tempData = tempData.filter(item => item.primary_skills.includes(selectedSkill));
    }

    if (search) {
      if (currentPage > 1) setCurrentPage(1);
      setSelectedCountry('');
      setSelectedSkill('');
      const lowerSearch = search.toLowerCase();
      tempData = tempData.filter(item =>
        searchKeys.some(key => 
          (getValueByPath(item, key)?.toString().toLowerCase() || '').includes(lowerSearch)
        )
      );
    }

    return tempData;
  }, [data, selectedCountry, selectedSkill, search, searchKeys]);

  const cityData = useMemo(() => {
    const cityMap = {};
    data.forEach(item => {
      const city = item.location.split(', ').pop();
      cityMap[city] = (cityMap[city] || 0) + 1;
    });

    return Object.keys(cityMap).map(city => ({
      name: city,
      value: cityMap[city],
    }));
  }, [data]);

  const totalPages = Math.ceil(filteredData.length / recordsPerPage);
  const currentData = filteredData.slice((currentPage - 1) * recordsPerPage, currentPage * recordsPerPage);

  return (
    <div className="w-[90%] mx-auto h-[100vh] overflow-hidden flex flex-col">
      {/* Filters & Search */}
      <div className="flex flex-wrap items-center gap-4 mb-2">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Country</label>
          <select
            value={selectedCountry}
            onChange={(e) => setSelectedCountry(e.target.value)}
            className="w-[180px] px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">All Countries</option>
            {countryOptions.map(country => (
              <option key={country} value={country}>{country}</option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Skill</label>
          <select
            value={selectedSkill}
            onChange={(e) => setSelectedSkill(e.target.value)}
            className="w-[180px] px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">All Skills</option>
            {skillOptions.map(skill => (
              <option key={skill} value={skill}>{skill}</option>
            ))}
          </select>
        </div>

        <SearchBar2 onChange={setSearch} />
      </div>

      {/* Grid Layout for Table & Pie Chart */}
      <div className="grid grid-cols-[2fr_1fr] gap-4 h-full">
        {/* Table Section */}
        <div className="flex flex-col h-full border border-gray-300 shadow-md rounded-lg p-4 overflow-hidden">
          {filteredData.length === 0 ? (
            <div className="text-center text-gray-500 text-lg mt-4">No results found</div>
          ) : (
            <>
              <div className="flex-grow  overflow-auto">
                <table className="ttable-auto w-full text-sm text-left border-collapse">
                  <thead className="bg-gray-100 text-gray-700 text-xs font-bold uppercase">
                    <tr>
                      {columns.map(col => (
                        <th key={col.key} className="px-4 py-3">{col.label}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {currentData.map((item, index) => (
                      <tr key={item.id || `${item.email}-${index}`} className="border-b hover:bg-gray-50 transition-all">
                        {columns.map(col => (
                          <td key={col.key} className="px-4 py-3">
                            {col.render ? col.render(getValueByPath(item, col.key), item) : getValueByPath(item, col.key)}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Pagination */}
              {filteredData.length > recordsPerPage && (
                <Stack spacing={2} direction="row" justifyContent="center" mb={7}>
                  <Pagination
                    count={totalPages}
                    page={currentPage}
                    onChange={(_, page) => setCurrentPage(page)}
                    shape="rounded"
                    variant="outlined"
                  />
                </Stack>
              )}
            </>
          )}
        </div>

        {/* Pie Chart Section */}
        <div className="border border-gray-300 shadow-md rounded-lg p-4 flex items-center justify-center">
          <CityPieChart data={cityData} />
        </div>
      </div>
    </div>
  );
};

export default ReusableTable;
