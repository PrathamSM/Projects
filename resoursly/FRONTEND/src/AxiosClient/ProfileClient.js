import axios from "axios";

const profileClient = axios.create({
    baseURL: 'http://localhost:8081/profiles',
    headers: {
        'Content-Type': 'application/json'
    }
});

export default profileClient;