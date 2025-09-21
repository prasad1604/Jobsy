import { Link, useNavigate } from 'react-router-dom';
import { useContext, useState } from "react";
import axios from "axios";
import { AppContext } from "../context/AppContext";
import { toast } from "react-toastify";

const Login = () => {
    const [isCreateAccount, setIsCreateAccount] = useState(false);
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const { backendURL, setIsLoggedIn, getUserData } = useContext(AppContext);
    const navigate = useNavigate();

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        axios.defaults.withCredentials = true;
        setLoading(true);

        try {
            if (isCreateAccount) {
                // register api
                const response = await axios.post(`${backendURL}/register`, { name, email, password });
                if (response.status === 201) {
                    navigate('/');
                    toast.success("Account created successfully.");
                } else {
                    toast.error("Email already exists.");
                }
            } else {
                // login api
                const response = await axios.post(`${backendURL}/login`, { email, password });
                if (response.status === 200) {
                    setIsLoggedIn(true);
                    getUserData();
                    navigate('/');
                } else {
                    toast.error("Invalid credentials.");
                }
            }
        } catch (error) {
            toast.error(error.response?.data?.message || "An error occurred");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <div className="row w-100">
                <div className="col-md-6 col-lg-4 mx-auto">
                    <div className="card shadow">
                        <div className="card-body p-4">
                            <div className="text-center mb-4">
                                <h3>{isCreateAccount ? 'Create Account' : 'Login'}</h3>
                            </div>

                            <form onSubmit={onSubmitHandler}>
                                {isCreateAccount && (
                                    <div className="mb-3">
                                        <label className="form-label">Full Name</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                            required
                                        />
                                    </div>
                                )}

                                <div className="mb-3">
                                    <label className="form-label">Email</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>

                                {!isCreateAccount && (
                                    <div className="mb-3 text-end">
                                        <Link to="/reset-password" className="text-decoration-none">
                                            Forgot Password?
                                        </Link>
                                    </div>
                                )}

                                <button 
                                    type="submit" 
                                    className="btn btn-primary w-100"
                                    disabled={loading}
                                >
                                    {loading ? 'Processing...' : (isCreateAccount ? 'Create Account' : 'Login')}
                                </button>
                            </form>

                            <div className="text-center mt-3">
                                {isCreateAccount ? (
                                    <>
                                        Already have an account?{" "}
                                        <span
                                            onClick={() => setIsCreateAccount(false)}
                                            className="text-decoration-underline"
                                            style={{ cursor: "pointer" }}
                                        >
                                            Login here
                                        </span>
                                    </>
                                ) : (
                                    <>
                                        Don't have an account?{" "}
                                        <span
                                            onClick={() => setIsCreateAccount(true)}
                                            className="text-decoration-underline"
                                            style={{ cursor: "pointer" }}
                                        >
                                            Sign up
                                        </span>
                                    </>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
