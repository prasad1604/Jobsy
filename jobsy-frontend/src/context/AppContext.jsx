import { createContext, useState } from "react";
import { AppConstants } from "../util/constants";
import { toast } from "react-toastify";
import axios from "axios";

export const AppContext = createContext();

export const  AppContextProvider = (props) =>{

    const backendURL = AppConstants.BACKEND_URL;
    const [isLoggedIn,setIsLoggedIn] = useState(false);
    const [userData,setUserData] = useState(false);

    const getUserData = async () => {
        try{
            const response = await axios.get(backendURL+"/profile");
            if(response.status === 200){
                setUserData(response.data);
            }
            else{
                toast.error("Unable to retrive the profile");
            }
        }
        catch(error){
            toast.error(error.message);
        }
    }

    const contextValue = {
        backendURL,
        isLoggedIn,
        setIsLoggedIn,
        userData,
        setUserData,
        getUserData
    }

    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )
}