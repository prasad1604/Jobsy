import { useNavigate } from 'react-router-dom';
import { useContext, useEffect, useRef, useState } from 'react';
import { AppContext } from '../context/AppContext';
import { USER_ROLES } from '../util/constants';
import { toast } from 'react-toastify';

const Menubar = () => {
    const navigate = useNavigate();
    const { userData, isLoggedIn, currentRole, userRoles, switchRole, logout, backendURL } = useContext(AppContext);
    const [dropDownOpen, setDropdownOpen] = useState(false);
    const dropDownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropDownRef.current && !dropDownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleLogout = async () => {
        const success = await logout();
        if (success) {
            navigate('/');
            toast.success("Logged out successfully");
        } else {
            toast.error("Failed to logout");
        }
    };

    const handleRoleSwitch = async (roleName) => {
        const success = await switchRole(roleName);
        if (success) {
            navigate('/dashboard');
        }
        setDropdownOpen(false);
    };

    const sendVerificationOtp = async () => {
        try {
            const response = await fetch(`${backendURL}/send-otp`, {
                method: 'POST',
                credentials: 'include'
            });
            if (response.ok) {
                navigate('/email-verify');
                toast.success("OTP has been sent successfully.");
            } else {
                toast.error("Unable to send OTP!");
            }
        } catch (err) {
            toast.error("Failed to send OTP" + err);
        }
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
            <div className="container">
                <div 
                    className="navbar-brand fw-bold text-primary"
                    onClick={() => navigate('/')}
                    style={{ cursor: 'pointer' }}
                >
                    Jobsy
                </div>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto">
                        <li className="nav-item">
                            <div 
                                className="nav-link"
                                onClick={() => navigate('/find-work')}
                                style={{ cursor: 'pointer' }}
                            >
                                Find Work
                            </div>
                        </li>
                        <li className="nav-item">
                            <div 
                                className="nav-link"
                                onClick={() => navigate('/find-talent')}
                                style={{ cursor: 'pointer' }}
                            >
                                Find Talent
                            </div>
                        </li>
                        {isLoggedIn && (
                            <li className="nav-item">
                                <div 
                                    className="nav-link"
                                    onClick={() => navigate('/my-jobs')}
                                    style={{ cursor: 'pointer' }}
                                >
                                    My Jobs
                                </div>
                            </li>
                        )}
                    </ul>

                    <div className="d-flex align-items-center">
                        {isLoggedIn && userData ? (
                            <div className="dropdown" ref={dropDownRef}>
                                <div 
                                    className="d-flex align-items-center"
                                    onClick={() => setDropdownOpen((prev) => !prev)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    {/* Role indicator */}
                                    {currentRole && (
                                        <span className="badge bg-primary me-2">
                                            {currentRole.role.roleName === USER_ROLES.FREELANCER ? 'Freelancer' : 'Hirer'}
                                        </span>
                                    )}
                                    
                                    {/* User avatar */}
                                    <div className="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px' }}>
                                        {userData.name[0].toUpperCase()}
                                    </div>
                                </div>

                                {dropDownOpen && (
                                    <div className="dropdown-menu dropdown-menu-end show position-absolute" style={{ right: 0, zIndex: 1000 }}>
                                        <div className="dropdown-header">
                                            <div className="fw-bold">{userData.name}</div>
                                            <small className="text-muted">{userData.email}</small>
                                        </div>
                                        
                                        {/* Role switching */}
                                        {userRoles.length > 1 && (
                                            <>
                                                <div className="dropdown-divider"></div>
                                                <h6 className="dropdown-header">Switch Mode</h6>
                                                {userRoles.map((userRole) => (
                                                    <button 
                                                        key={userRole.role.id}
                                                        className="dropdown-item"
                                                        onClick={() => handleRoleSwitch(userRole.role.roleName)}
                                                        disabled={userRole.isActive}
                                                    >
                                                        {userRole.role.roleName === USER_ROLES.FREELANCER ?
                                                            '🎯 Work as Freelancer' : '💼 Hire Talent'}
                                                        {userRole.isActive && <span className="text-success ms-2">✓</span>}
                                                    </button>
                                                ))}
                                            </>
                                        )}

                                        <div className="dropdown-divider"></div>
                                        
                                        {/* Navigation items */}
                                        <button 
                                            className="dropdown-item"
                                            onClick={() => navigate('/dashboard')}
                                        >
                                            📊 Dashboard
                                        </button>
                                        <button 
                                            className="dropdown-item"
                                            onClick={() => navigate('/profile')}
                                        >
                                            👤 Profile
                                        </button>
                                        <button 
                                            className="dropdown-item"
                                            onClick={() => navigate('/messages')}
                                        >
                                            💬 Messages
                                        </button>
                                        
                                        {!userData.isAccountVerified && (
                                            <>
                                                <div className="dropdown-divider"></div>
                                                <button 
                                                    className="dropdown-item text-warning"
                                                    onClick={sendVerificationOtp}
                                                >
                                                    ⚠️ Verify Email
                                                </button>
                                            </>
                                        )}
                                        
                                        <div className="dropdown-divider"></div>
                                        <button 
                                            className="dropdown-item text-danger"
                                            onClick={handleLogout}
                                        >
                                            🚪 Logout
                                        </button>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <div className="d-flex gap-2">
                                <button 
                                    className="btn btn-outline-primary"
                                    onClick={() => navigate('/login')}
                                >
                                    Login
                                </button>
                                <button 
                                    className="btn btn-primary"
                                    onClick={() => navigate('/login')}
                                >
                                    Sign Up
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Menubar;
