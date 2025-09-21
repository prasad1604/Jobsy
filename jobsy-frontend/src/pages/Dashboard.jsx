import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppContext } from '../context/AppContext';
import { USER_ROLES } from '../util/constants';
import axios from 'axios';

const Dashboard = () => {
    const navigate = useNavigate();
    const { backendURL, userData, currentRole, isLoggedIn, assignRole } = useContext(AppContext);
    const [dashboardData, setDashboardData] = useState(null);
    const [loading, setLoading] = useState(true);

    // Redirect if not logged in
    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }
        fetchDashboardData();
    }, [isLoggedIn, navigate, currentRole]);

    const fetchDashboardData = async () => {
        try {
            const response = await axios.get(`${backendURL}/api/dashboard`);
            if (response.status === 200) {
                setDashboardData(response.data);
            }
        } catch (error) {
            console.error('Error fetching dashboard data:', error);
        } finally {
            setLoading(false);
        }
    };

    // Show role selection if user has no roles
    const RoleSelection = () => (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card shadow">
                        <div className="card-body text-center p-5">
                            <h2 className="mb-4">Welcome to Jobsy!</h2>
                            <p className="text-muted mb-4">Choose how you'd like to get started:</p>
                            
                            <div className="row g-4">
                                <div className="col-md-6">
                                    <div className="card h-100 border-primary">
                                        <div className="card-body d-flex flex-column">
                                            <div className="text-primary mb-3">
                                                <i className="fas fa-search fa-3x"></i>
                                            </div>
                                            <h5 className="card-title">Work as Freelancer</h5>
                                            <p className="card-text flex-grow-1">Find projects and showcase your skills to potential clients</p>
                                            <button 
                                                className="btn btn-primary"
                                                onClick={() => assignRole(USER_ROLES.FREELANCER)}
                                            >
                                                Start as Freelancer
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                
                                <div className="col-md-6">
                                    <div className="card h-100 border-success">
                                        <div className="card-body d-flex flex-column">
                                            <div className="text-success mb-3">
                                                <i className="fas fa-briefcase fa-3x"></i>
                                            </div>
                                            <h5 className="card-title">Hire Talent</h5>
                                            <p className="card-text flex-grow-1">Find skilled freelancers for your projects</p>
                                            <button 
                                                className="btn btn-success"
                                                onClick={() => assignRole(USER_ROLES.HIRER)}
                                            >
                                                Start Hiring
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '60vh' }}>
                <div className="text-center">
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                    <p className="mt-3">Loading dashboard...</p>
                </div>
            </div>
        );
    }

    // Show role selection if user has no current role
    if (!currentRole) {
        return <RoleSelection />;
    }

    // Freelancer Dashboard
    if (currentRole.role.roleName === USER_ROLES.FREELANCER) {
        return (
            <div className="container mt-4">
                <div className="row">
                    <div className="col-12">
                        <h2>Welcome back, {userData?.name}!</h2>
                        <p className="text-muted">Here's your freelancer dashboard</p>
                    </div>
                </div>
                
                <div className="row g-4">
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Active Proposals</h5>
                                <h3 className="text-primary">{dashboardData?.activeProposals || 0}</h3>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Completed Jobs</h5>
                                <h3 className="text-success">{dashboardData?.completedJobs || 0}</h3>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Messages</h5>
                                <h3 className="text-info">
                                    {dashboardData?.unreadMessages > 0 ? `${dashboardData.unreadMessages} unread messages` : 'No new messages'}
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div className="row mt-4">
                    <div className="col-12">
                        <div className="card">
                            <div className="card-header">
                                <h5>Recent Job Opportunities</h5>
                            </div>
                            <div className="card-body">
                                <p>Loading recent jobs...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    // Hirer Dashboard
    if (currentRole.role.roleName === USER_ROLES.HIRER) {
        return (
            <div className="container mt-4">
                <div className="row">
                    <div className="col-12">
                        <h2>Welcome back, {userData?.name}!</h2>
                        <p className="text-muted">Here's your hirer dashboard</p>
                    </div>
                </div>
                
                <div className="row g-4">
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Active Jobs</h5>
                                <h3 className="text-primary">{dashboardData?.activeJobs || 0}</h3>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Completed Projects</h5>
                                <h3 className="text-success">{dashboardData?.completedProjects || 0}</h3>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-body text-center">
                                <h5>Messages</h5>
                                <h3 className="text-info">
                                    {dashboardData?.unreadMessages > 0 ? `${dashboardData.unreadMessages} unread messages` : 'No new messages'}
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div className="row mt-4">
                    <div className="col-12">
                        <div className="card">
                            <div className="card-header">
                                <h5>Your Recent Job Posts</h5>
                            </div>
                            <div className="card-body">
                                <p>Loading your recent job posts...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return null;
};

export default Dashboard;
