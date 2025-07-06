import axios from "axios";
import Cookies from "js-cookie";
import AuthService from "./../services/auth/AuthService";

export const client = axios.create({
    baseURL: "http://localhost:8081/api",
    withCredentials: true,
});

client.interceptors.request.use(
    config => {
        const token = Cookies.get("accessToken");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

client.interceptors.response.use(
    response => response,
    async error => {
        const {response, config} = error;

        if (response && response.status === 401 && !config._retried) {
            config._retried = true;
            const success = await AuthService.refresh();

            if (success) {
                const newToken = Cookies.get("accessToken");
                if (newToken) {
                    config.headers["Authorization"] = `Bearer ${newToken}`;
                }
                return client(config);
            }
        }

        return Promise.reject(error);
    }
);

