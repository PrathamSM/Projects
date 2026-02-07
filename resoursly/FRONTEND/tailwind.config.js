/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: { // Correct placement for custom colors
        'custom-light': '#E8EDF0',
      },
      fontFamily: {
        roboto: ["Roboto", "sans-serif"],
      },
      fontSize: {
        xs: '12px', // Mapping font-size to 12px
      },
      lineHeight: {
        '18': '18px', // Adding custom line-height of 18px
      },
      letterSpacing: {
        normal: '0%', // Default letter-spacing, equivalent to 0%
      },
      fontWeight: {
        semibold: '600', // Defining custom weight 600
      },
    },
  },
  plugins: [],
}

