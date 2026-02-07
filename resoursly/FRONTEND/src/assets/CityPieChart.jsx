import React from 'react';
import { PieChart, Pie, Tooltip, Cell, Legend, Label } from 'recharts';

// Colors for different slices
const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#AF19FF'];

const CityPieChart = ({ data }) => {
  return (
    <div className="flex flex-col items-center"> {/* Centered vertically */}
      <div className="text-center font-bold text-lg mb-2">Profiles Per Country</div>
      <PieChart width={300} height={300}>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          outerRadius={120}
          innerRadius={50} 
          fill="#8884d8"
          dataKey="value"
          label
        >
          {data.map((_, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </div>
  );
};

export default CityPieChart;

