import axios from 'axios';
import {useNavigate} from "react-router-dom";

const api = axios.create({
    baseURL: 'https://wordle-4fel.onrender.com',
});

api.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            config.headers.Authorization = `${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Add a response interceptor
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const refreshToken = localStorage.getItem('refreshToken');
                const response = await axios.post('/auth/refresh', { refreshToken });
                const { access_token, refresh_token, role } = response.data;
                localStorage.setItem('accessToken', access_token);
                localStorage.setItem('refreshToken', refresh_token);
                localStorage.setItem('role', role);
                // Retry the original request with the new token
                originalRequest.headers.Authorization = `${access_token}`;
                return axios(originalRequest);
            } catch (error) {
                // Handle refresh token error or redirect to login
                console.log('Error with refresh token:', error);
                localStorage.clear();
                useNavigate()('/');
                window.location.reload();
            }
        }

        return Promise.reject(error);
    }
);


export default api
