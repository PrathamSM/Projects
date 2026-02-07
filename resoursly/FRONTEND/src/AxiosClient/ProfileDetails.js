import axios from "axios";

const ProfileDetails = axios.create({
    baseURL: 'http://localhost:8090/profiles',
    headers: {
        'Content-Type': 'application/json'
    }
});

export default ProfileDetails;