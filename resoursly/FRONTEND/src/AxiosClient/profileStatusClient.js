import axios from "axios";

const profileStatusClient = axios.create({
    baseURL: 'http://localhost:8085/profile-status',
    headers: {
        'Content-Type': 'application/json'
    }
});

export default profileStatusClient;