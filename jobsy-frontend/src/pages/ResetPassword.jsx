import { Link, useNavigate } from "react-router-dom";
import { useContext, useRef, useState } from "react";
import { AppContext } from "../context/AppContext";
import axios from "axios";
import { toast } from "react-toastify";

const ResetPassword = () => {
    const inputRef = useRef([]);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [email, setEmail] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [isEmailSent, setIsEmailSent] = useState(false);
    const [otp, setOtp] = useState("");
    const [isOtpSubmitted, setIsOtpSubmitted] = useState(false);
    const { backendURL } = useContext(AppContext);

    axios.defaults.withCredentials = true;

    const handleChange = (e, index) => {
        const value = e.target.value.replace(/\D/, "");
        e.target.value = value;
        if (value && index < 5) {
            inputRef.current[index + 1].focus();
        } else if (!value && index > 0) {
            inputRef.current[index - 1].focus();
        }
    };

    const handleKeyDown = (e, index) => {
        if (e.key === "Backspace" && !e.target.value && index > 0) {
            inputRef.current[index - 1].focus();
        }
    };

    const handlePaste = (e) => {
        e.preventDefault();
        const paste = e.clipboardData.getData("text").slice(0, 6).split("");
        paste.forEach((digit, i) => {
            if (inputRef.current[i]) {
                inputRef.current[i].value = digit;
            }
        });
        const next = paste.length < 6 ? paste.length : 5;
        inputRef.current[next].focus();
    };

    const onSubmitEmail = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await axios.post(backendURL + "/send-reset-otp?email=" + email);
            if (response.status === 200) {
                toast.success("Password reset otp sent successfully");
                setIsEmailSent(true);
            } else {
                toast.error("Something went wrong please try again");
            }
        } catch (error) {
            toast.error(error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleVerify = () => {
        const otpValue = inputRef.current.map(input => input.value).join("");
        if (otpValue.length !== 6) {
            toast.error("Please enter all 6 digits of the otp");
            return;
        }
        setOtp(otpValue);
        setIsOtpSubmitted(true);
    };

    const onSubmitNewPassword = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await axios.post(backendURL + "/reset-password", { email, otp, newPassword });
            if (response.status === 200) {
                toast.success("Password reset successfully");
                navigate("/login");
            } else {
                toast.error("Something went wrong, please try again");
            }
        } catch (error) {
            toast.error(error.message);
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
                                <h3>Reset Password</h3>
                            </div>

                            {!isEmailSent ? (
                                <form onSubmit={onSubmitEmail}>
                                    <div className="mb-3">
                                        <label className="form-label">Enter your registered email address</label>
                                        <input
                                            type="email"
                                            className="form-control"
                                            value={email}
                                            onChange={(e) => setEmail(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <button 
                                        type="submit" 
                                        className="btn btn-primary w-100"
                                        disabled={loading}
                                    >
                                        {loading ? 'Sending...' : 'Send OTP'}
                                    </button>
                                </form>
                            ) : !isOtpSubmitted ? (
                                <div>
                                    <div className="mb-3">
                                        <label className="form-label">Enter the 6 digit OTP sent to your email address</label>
                                        <div className="d-flex gap-2 justify-content-center">
                                            {Array.from({ length: 6 }, (_, index) => (
                                                <input
                                                    key={index}
                                                    ref={(el) => (inputRef.current[index] = el)}
                                                    type="text"
                                                    className="form-control text-center"
                                                    style={{ width: '50px', height: '50px' }}
                                                    maxLength="1"
                                                    onChange={(e) => handleChange(e, index)}
                                                    onKeyDown={(e) => handleKeyDown(e, index)}
                                                    onPaste={handlePaste}
                                                />
                                            ))}
                                        </div>
                                    </div>
                                    <button 
                                        type="button"
                                        className="btn btn-primary w-100"
                                        onClick={handleVerify}
                                    >
                                        Verify OTP
                                    </button>
                                </div>
                            ) : (
                                <form onSubmit={onSubmitNewPassword}>
                                    <div className="mb-3">
                                        <label className="form-label">Enter the new password below</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            value={newPassword}
                                            onChange={(e) => setNewPassword(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <button 
                                        type="submit" 
                                        className="btn btn-primary w-100"
                                        disabled={loading}
                                    >
                                        {loading ? 'Updating...' : 'Reset Password'}
                                    </button>
                                </form>
                            )}

                            <div className="text-center mt-3">
                                <Link to="/login" className="text-decoration-none">
                                    Back to Login
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
