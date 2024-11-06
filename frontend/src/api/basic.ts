import axios from 'axios';
const serverUrl = process.env.NEXT_PUBLIC_SERVER_URL;

const api = axios.create({
    baseURL: serverUrl,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        Accept: 'application/json',
    },
});

export default api;
