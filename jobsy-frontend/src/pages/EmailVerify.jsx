import { useContext, useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AppContext } from "../context/AppContext";
import { toast } from "react-toastify";
import axios from "axios";

const EmailVerify = () => {
    const inputRef = useRef([]);
    const [loading, setLoading] = useState(false);
    const { getUserData, isLoggedIn, userData, backendURL } = useContext(AppContext);
    const navigate = useNavigate();

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

    const handleVerify = async () => {
        const otpValue = inputRef.current.map(input => input.value).join("");
        if (otpValue.length !== 6) {
            toast.error("Please enter all 6 digits of the OTP");
            return;
        }
        setLoading(true);
        try {
            const response = await axios.post(backendURL + "/verify-otp", { otp: otpValue });
            if (response.status === 200) {
                toast.success("OTP verified successfully");
                getUserData();
                navigate("/");
            } else {
                toast.error("Invalid OTP");
            }
        } catch (error) {
            toast.error("Failed to verify OTP. Please try again." + error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (isLoggedIn && userData && userData.isAccountVerified) {
            navigate("/");
        }
    }, [isLoggedIn, userData, navigate]);

    return (
        <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <div className="row w-100">
                <div className="col-md-6 col-lg-4 mx-auto">
                    <div className="card shadow">
                        <div className="card-body p-4">
                            <div className="text-center mb-4">
                                <h3>Email Verification</h3>
                                <p className="text-muted">Enter the 6 digit OTP sent to your email address</p>
                            </div>

                            <div className="mb-3">
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
                                disabled={loading}
                            >
                                {loading ? 'Verifying...' : 'Verify Email'}
                            </button>

                            <div className="text-center mt-3">
                                <Link to="/" className="text-decoration-none">
                                    Back to Home
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default EmailVerify;
