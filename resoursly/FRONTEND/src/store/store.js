import { configureStore } from "@reduxjs/toolkit";
import popupReducer from '../feature/PopupSlice'
import activeProfileReducer from '../feature/CurrentProfile'
import editProfileReducer from '../feature/EditMode'

const store = configureStore({
    reducer : {
        popup: popupReducer,
        activeProfile : activeProfileReducer,
        editProfile: editProfileReducer,
    }
});


export default store;