import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AppContext } from "../context/context";

const Navbar = () => {

  const navigate = useNavigate();
  const { user, setUser, clearUser } = useContext(AppContext);

  if (!user) return null;

  const handleLogout = () => {
    clearUser();
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
  };

  const handleSwitchRole = () => {
    const newRole = user.activeRole === "FREELANCER" ? "CLIENT" : "FREELANCER";

    const updatedUser = { ...user, activeRole: newRole };

    // Update context
    setUser(updatedUser);

    // Update localStorage
    localStorage.setItem("user", JSON.stringify(updatedUser));

    // Optional redirect
    navigate("/dashboard");
  };

  return (
    <nav
      className="
        fixed top-0 left-0 right-0 z-50 text-white
        bg-gradient-to-r from-[#0f051d] via-[#1a0b2e] to-[#0f051d]
        backdrop-blur-md
        border-b border-purple-900/20
      "
    >
      <div className="flex justify-between items-center px-12 py-5 max-w-7xl mx-auto">

        {/* LOGO */}
        <div
          onClick={() => navigate("/dashboard")}
          className="text-2xl font-bold tracking-wide cursor-pointer hover:text-purple-400 transition"
        >
          Jobsy
        </div>

        {/* LINKS */}
        <div className="hidden md:flex gap-12 text-[15px] font-medium">

          <button
            onClick={() => navigate("/dashboard")}
            className="hover:text-purple-400 transition"
          >
            Dashboard
          </button>

        </div>

        {/* RIGHT SIDE */}
        <div className="flex items-center gap-6">

          {/* USER NAME + ROLE */}
          <span className="text-sm text-gray-300">
            {user?.fullName} ({user?.activeRole})
          </span>

          {/* SWITCH ROLE BUTTON */}
          <button
            onClick={handleSwitchRole}
            className="
              bg-gray-700 hover:bg-gray-600
              transition
              px-4 py-2 rounded-lg
              text-sm font-medium
            "
          >
            Switch Role
          </button>

          {/* LOGOUT */}
          <button
            onClick={handleLogout}
            className="
              bg-purple-600 hover:bg-purple-700
              transition
              px-5 py-2 rounded-lg
              text-sm font-medium
            "
          >
            Logout
          </button>

        </div>
      </div>
    </nav>
  );
};

export default Navbar;