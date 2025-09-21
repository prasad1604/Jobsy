import { createContext, useEffect, useState } from "react";
import { BACKEND_URL} from "../util/constants";
import { toast } from "react-toastify";
import axios from "axios";

export const AppContext = createContext();

export const AppContextProvider = (props) => {
    axios.defaults.withCredentials = true;
    const backendURL = BACKEND_URL;
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);
    const [userRoles, setUserRoles] = useState([]);
    const [currentRole, setCurrentRole] = useState(null);
    const [loading, setLoading] = useState(true);

    const getUserData = async () => {
        try {
            const response = await axios.get(backendURL + "/profile");
            if (response.status === 200) {
                setUserData(response.data);
                await getUserRoles();
            } else {
                toast.error("Unable to retrieve the profile");
            }
        } catch (error) {
            console.error("Error getting user data:", error);
        }
    };

    const getUserRoles = async () => {
        try {
            const response = await axios.get(backendURL + "/api/roles/my-roles");
            if (response.status === 200) {
                setUserRoles(response.data);
                const activeRole = response.data.find(role => role.isActive);
                setCurrentRole(activeRole);
            }
        } catch (error) {
            console.error("Error getting user roles:", error);
        }
    };

    const switchRole = async (roleName) => {
        try {
            const response = await axios.post(backendURL + "/api/roles/switch", {
                roleName: roleName
            });
            if (response.status === 200) {
                await getUserRoles();
                toast.success(`Switched to ${roleName} mode`);
                return true;
            }
        } catch (error) {
            toast.error("Failed to switch role");
            console.error("Error switching role:", error);
            return false;
        }
    };

    const assignRole = async (roleName) => {
        try {
            const response = await axios.post(backendURL + "/api/roles/assign", {
                roleName: roleName
            });
            if (response.status === 200) {
                await getUserRoles();
                toast.success(`${roleName} role assigned successfully`);
                return true;
            }
        } catch (error) {
            toast.error("Failed to assign role");
            console.error("Error assigning role:", error);
            return false;
        }
    };

    const getAuthState = async () => {
        try {
            const response = await axios.get(backendURL + "/is-authenticated");
            if (response.status === 200 && response.data === true) {
                setIsLoggedIn(true);
                await getUserData();
            } else {
                setIsLoggedIn(false);
            }
        } catch (error) {
            setIsLoggedIn(false);
            console.error("Auth check error:", error);
        } finally {
            setLoading(false);
        }
    };

    const logout = async () => {
        try {
            const response = await axios.post(backendURL + "/logout");
            if (response.status === 200) {
                setIsLoggedIn(false);
                setUserData(false);
                setUserRoles([]);
                setCurrentRole(null);
                return true;
            }
        } catch (error) {
            console.error("Logout error:", error);
            return false;
        }
    };

    // Check if user has a specific role
    const hasRole = (roleName) => {
        return userRoles.some(role => role.role.roleName === roleName);
    };

    // Check if user is currently in a specific role
    const isCurrentRole = (roleName) => {
        return currentRole?.role.roleName === roleName;
    };

    useEffect(() => {
        getAuthState();
    }, []);

    const contextValue = {
        backendURL,
        isLoggedIn,
        setIsLoggedIn,
        userData,
        setUserData,
        getUserData,
        userRoles,
        currentRole,
        switchRole,
        assignRole,
        hasRole,
        isCurrentRole,
        loading,
        logout
    };

    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    );
};
