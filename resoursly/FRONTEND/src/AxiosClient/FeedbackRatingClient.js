import axios from "axios";

const feedbackRatingClient = axios.create({
    baseURL: 'http://localhost:8084/feedbacks',
    headers: {
        'Content-Type': 'application/json'
    }
});

export default feedbackRatingClient;