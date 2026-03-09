import { useState } from "react";
import { AppContext } from "./context";

const AppContextProvider = ({ children }) => {

  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  });

  const clearUser = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AppContext.Provider value={{ user, setUser, clearUser }}>
      {children}
    </AppContext.Provider>
  );
};

export default AppContextProvider;