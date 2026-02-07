import axios from "axios";

const proExpClient = axios.create({
    baseURL: 'http://localhost:8082/professional-experiences',
    headers: {
        'Content-Type': 'application/json'
    }
});

export default proExpClient;