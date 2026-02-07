import { createSlice } from "@reduxjs/toolkit";
 
const initialState = {
    isEdit : false
}
 
const editProfileSlice = createSlice({
    name: "editProfile",
    initialState,
    reducers: {
        editProfileOn: (state) => {
           state.isEdit = true 
        },
 
        editProfileOff: (state) => {
            state.isEdit = false
        },
     }
})
 
export const { editProfileOn, editProfileOff} = editProfileSlice.actions;
export default editProfileSlice.reducer;