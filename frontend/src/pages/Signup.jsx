import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { validateEmail } from "../util/validation.js";
import axiosConfig from "../util/axiosConfig.js";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import { toast } from "react-hot-toast";

const Signup = () => {

  const navigate = useNavigate();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [activeRole, setActiveRole] = useState("FREELANCER");

  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {

    e.preventDefault();

    setIsLoading(true);
    setError(null);

    // VALIDATIONS

    if (!fullName.trim()) {
      setError("Please enter your full name");
      setIsLoading(false);
      return;
    }

    if (!validateEmail(email)) {
      setError("Please enter a valid email");
      setIsLoading(false);
      return;
    }

    if (!password.trim()) {
      setError("Please enter your password");
      setIsLoading(false);
      return;
    }

    try {

      const response = await axiosConfig.post(
        API_ENDPOINTS.REGISTER,
        {
          fullName,
          email,
          password,
          activeRole
        }
      );

      if (response.status === 200 || response.status === 201) {

        toast.success("Account created successfully");

        navigate("/login");

      }

    } catch (err) {

      const message =
        err.response?.data?.message ||
        "Signup failed. Please try again.";

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

      {/* OVERLAY */}
      <div className="absolute inset-0 bg-black/80" />

      {/* GRADIENT */}
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
            Create your account
          </h2>

          <form onSubmit={handleSubmit} className="space-y-5">

            {/* FULL NAME */}
            <div>
              <label className="text-sm text-gray-300">
                Full Name
              </label>

              <input
                type="text"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                placeholder="Enter full name"
                className="w-full mt-1 px-4 py-2.5 rounded-lg bg-black/50 border border-gray-600 focus:border-purple-500 focus:outline-none"
              />
            </div>

            {/* EMAIL */}
            <div>
              <label className="text-sm text-gray-300">
                Email
              </label>

              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter email"
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
                placeholder="Enter password"
                className="w-full mt-1 px-4 py-2.5 rounded-lg bg-black/50 border border-gray-600 focus:border-purple-500 focus:outline-none"
              />
            </div>

            {/* ROLE */}
            <div>
              <label className="text-sm text-gray-300">
                Select Role
              </label>

              <div className="flex gap-6 mt-2">

                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    value="FREELANCER"
                    checked={activeRole === "FREELANCER"}
                    onChange={(e) => setActiveRole(e.target.value)}
                  />
                  Freelancer
                </label>

                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    value="HIRER"
                    checked={activeRole === "HIRER"}
                    onChange={(e) => setActiveRole(e.target.value)}
                  />
                  Hirer
                </label>

              </div>
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
              className={`w-full bg-purple-600 hover:bg-purple-700 transition py-3 rounded-lg font-medium ${
                isLoading ? "opacity-60 cursor-not-allowed" : ""
              }`}
            >
              {isLoading ? "Creating account..." : "Sign Up"}
            </button>

          </form>

          {/* FOOTER */}
          <p className="text-gray-400 text-sm text-center mt-6">
            Already have an account?{" "}
            <span
              onClick={() => navigate("/login")}
              className="text-purple-400 cursor-pointer hover:underline"
            >
              Login
            </span>
          </p>

        </div>

      </div>

    </div>
  );
};

export default Signup;
