import { createSlice } from "@reduxjs/toolkit"

const initialState = {
    isVisible : false,
    isReadOnly : false
}

const popupSlice = createSlice({
    name: 'popup',
    initialState,
    reducers: {
        showProfile: (state) => {
            state.isVisible = true;
        },

        hideProfile: (state) => {
            state.isVisible = false;
        },
        viewOnlyProfile: (state) => {
            state.isReadOnly = true;
        },
        viewOnlyProfileFalse: (state) => {
            state.isReadOnly = false;
        }
     }
})


export const { showProfile, hideProfile, viewOnlyProfile, viewOnlyProfileFalse } = popupSlice.actions;
export default popupSlice.reducer;