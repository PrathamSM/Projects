import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    proId : 0
}

const currentProfileSlice = createSlice({
    name: 'activeProfile',
    initialState,
    reducers: {
        setProfileId: (state, action) => {
           state.proId = action.payload;   
        },

        resetProfileId: (state) => {
            state.proId = 0;
            state.profession = null;
        },

        setProfession: (state, action) => {
            state.profession = action.payload; // Update the profession
          },
     }
})

export const { setProfileId, resetProfileId, setProfession} = currentProfileSlice.actions;
export default currentProfileSlice.reducer;