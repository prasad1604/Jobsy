import axios from "axios";
import { BASE_URL } from "./apiEndpoints";

const axiosConfig = axios.create({
    baseURL: BASE_URL,
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json"
    }
});

// endpoints that do not require Authorization header
const excludeEndpoints = ["/login", "/register", "/status"];


// REQUEST INTERCEPTOR
axiosConfig.interceptors.request.use(

    (config) => {

        const url = config.url || "";

        const shouldSkipToken =
            excludeEndpoints.some(endpoint => url.includes(endpoint));

        if (!shouldSkipToken) {

            const token = localStorage.getItem("token");

            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }

        }

        return config;
    },

    (error) => Promise.reject(error)

);


// RESPONSE INTERCEPTOR
axiosConfig.interceptors.response.use(

    (response) => response,

    (error) => {

        const url = error.config?.url || "";

        const isAuthEndpoint =
            excludeEndpoints.some(endpoint => url.includes(endpoint));

        if (!isAuthEndpoint && error.response?.status === 401) {

            localStorage.removeItem("token");

            window.location.href = "/login";

        }

        return Promise.reject(error);

    }

);

export default axiosConfig;
