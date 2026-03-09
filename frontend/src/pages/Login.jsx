import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { validateEmail } from "../util/validation.js";
import axiosConfig from "../util/axiosConfig.js";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import { AppContext } from "../context/context.jsx";
import { toast } from "react-hot-toast";

const Login = () => {

  const navigate = useNavigate();
  const { setUser } = useContext(AppContext);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {

    e.preventDefault();

    setIsLoading(true);
    setError(null);

    // EMAIL VALIDATION
    if (!validateEmail(email)) {
      setError("Please enter a valid email address");
      setIsLoading(false);
      return;
    }

    // PASSWORD VALIDATION
    if (!password.trim()) {
      setError("Please enter your password");
      setIsLoading(false);
      return;
    }

    try {

      const response = await axiosConfig.post(
        API_ENDPOINTS.LOGIN,
        {
          email,
          password
        }
      );

      // STORE TOKEN
      localStorage.setItem("token", response.data.token);

      // STORE USER (safe fields only)
      if (response.data.user) {

        const safeUser = {
          id: response.data.user.id,
          fullName: response.data.user.fullName,
          email: response.data.user.email,
          profileImageUrl: response.data.user.profileImageUrl,
          activeRole: response.data.user.activeRole
        };

        localStorage.setItem("user", JSON.stringify(safeUser));

        setUser(safeUser);
      }

      toast.success("Login successful");

      navigate("/dashboard");

    } catch (err) {

      const message =
        err.response?.data?.message ||
        "Login failed. Please try again.";

      setError(message);

      toast.error(message);

    } finally {

      setIsLoading(false);

    }
  };

  return (
    <div className="relative min-h-screen text-white flex items-center justify-center">

      {/* BACKGROUND */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: `url('/hero.png')`,
        }}
      />

      {/* DARK OVERLAY */}
      <div className="absolute inset-0 bg-black/80" />

      {/* PURPLE GRADIENTS */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,rgba(88,28,135,0.45),transparent_55%)]" />
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_bottom_right,rgba(88,28,135,0.35),transparent_50%)]" />

      {/* CONTENT */}
      <div className="relative z-10 w-full max-w-md px-6">

        {/* LOGO */}
        <div
          onClick={() => navigate("/")}
          className="text-3xl font-bold text-center mb-8 cursor-pointer hover:text-purple-400"
        >
          Jobsy
        </div>

        {/* CARD */}
        <div className="bg-black/40 backdrop-blur-md border border-gray-700 rounded-xl p-8 shadow-lg">

          <h2 className="text-2xl font-semibold mb-6 text-center">
            Login to your account
          </h2>

          <form onSubmit={handleSubmit} className="space-y-5">

            {/* EMAIL */}
            <div>
              <label className="text-sm text-gray-300">
                Email
              </label>

              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email"
                className="w-full mt-1 px-4 py-2.5 rounded-lg bg-black/50 border border-gray-600 focus:border-purple-500 focus:outline-none"
              />
            </div>

            {/* PASSWORD */}
            <div>
              <label className="text-sm text-gray-300">
                Password
              </label>

              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
                className="w-full mt-1 px-4 py-2.5 rounded-lg bg-black/50 border border-gray-600 focus:border-purple-500 focus:outline-none"
              />
            </div>

            {/* ERROR */}
            {error && (
              <p className="text-red-400 text-sm text-center">
                {error}
              </p>
            )}

            {/* BUTTON */}
            <button
              type="submit"
              disabled={isLoading}
              className={`w-full bg-purple-600 hover:bg-purple-700 transition py-3 rounded-lg font-medium mt-2 ${
                isLoading ? "opacity-60 cursor-not-allowed" : ""
              }`}
            >
              {isLoading ? "Logging in..." : "Login"}
            </button>

          </form>

          {/* FOOTER */}
          <p className="text-gray-400 text-sm text-center mt-6">
            Don’t have an account?{" "}
            <span
              onClick={() => navigate("/signup")}
              className="text-purple-400 cursor-pointer hover:underline"
            >
              Sign up
            </span>
          </p>

        </div>

      </div>

    </div>
  );
};

export default Login;
