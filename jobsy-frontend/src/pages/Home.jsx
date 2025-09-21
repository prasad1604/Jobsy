import Header from "../components/Header";
import { useContext } from "react";
import { AppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";
import { JOB_CATEGORIES } from "../util/constants";

const Home = () => {
    const { isLoggedIn } = useContext(AppContext);
    const navigate = useNavigate();

    return (
        <div>
            <Header />
            
            {/* How it works section */}
            <div className="container py-5">
                <div className="row">
                    <div className="col-12 text-center mb-5">
                        <h2>Get started in just a few simple steps</h2>
                    </div>
                    
                    <div className="col-md-4 text-center mb-4">
                        <div className="mb-3">
                            <i className="fas fa-user-plus fa-3x text-primary"></i>
                        </div>
                        <h5>1. Sign Up</h5>
                        <p>Sign up and build your professional profile to showcase your skills</p>
                    </div>
                    
                    <div className="col-md-4 text-center mb-4">
                        <div className="mb-3">
                            <i className="fas fa-search fa-3x text-primary"></i>
                        </div>
                        <h5>2. Find Work</h5>
                        <p>Browse thousands of projects or post your own job requirements</p>
                    </div>
                    
                    <div className="col-md-4 text-center mb-4">
                        <div className="mb-3">
                            <i className="fas fa-handshake fa-3x text-primary"></i>
                        </div>
                        <h5>3. Get Paid</h5>
                        <p>Submit proposals, get hired, and receive secure payments</p>
                    </div>
                </div>
            </div>

            {/* Categories section */}
            <div className="bg-light py-5">
                <div className="container">
                    <div className="row">
                        <div className="col-12 text-center mb-5">
                            <h2>Explore opportunities across different industries</h2>
                        </div>
                        
                        <div className="row g-3">
                            {JOB_CATEGORIES.slice(0, 8).map((category, index) => (
                                <div key={index} className="col-md-3 col-sm-6">
                                    <div className="card h-100 text-center">
                                        <div className="card-body">
                                            <h6 className="card-title">{category}</h6>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                        
                        <div className="col-12 text-center mt-4">
                            <button 
                                className="btn btn-primary"
                                onClick={() => navigate('/find-work')}
                            >
                                View All Categories
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Features section */}
            <div className="container py-5">
                <div className="row align-items-center">
                    <div className="col-lg-6 mb-4">
                        <h2>Find skilled professionals</h2>
                        <p className="lead">The best platform for freelancers and businesses</p>
                        <div className="row g-4">
                            <div className="col-12">
                                <div className="d-flex">
                                    <div className="flex-shrink-0">
                                        <i className="fas fa-shield-alt fa-2x text-success"></i>
                                    </div>
                                    <div className="flex-grow-1 ms-3">
                                        <h6>Secure Payments</h6>
                                        <p>Safe and secure payment processing with escrow protection for all transactions.</p>
                                    </div>
                                </div>
                            </div>
                            
                            <div className="col-12">
                                <div className="d-flex">
                                    <div className="flex-shrink-0">
                                        <i className="fas fa-star fa-2x text-warning"></i>
                                    </div>
                                    <div className="flex-grow-1 ms-3">
                                        <h6>Vetted Professionals</h6>
                                        <p>Access to pre-vetted professionals with proven track records and high ratings.</p>
                                    </div>
                                </div>
                            </div>
                            
                            <div className="col-12">
                                <div className="d-flex">
                                    <div className="flex-shrink-0">
                                        <i className="fas fa-headset fa-2x text-info"></i>
                                    </div>
                                    <div className="flex-grow-1 ms-3">
                                        <h6>24/7 Support</h6>
                                        <p>Round-the-clock customer support to help you with any questions or issues.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-lg-6 text-center">
                        <div className="bg-primary text-white p-5 rounded">
                            <h4>Ready to get started?</h4>
                            <p>Join thousands of freelancers and businesses already using Jobsy</p>
                            {!isLoggedIn && (
                                <div className="d-grid gap-2 d-md-flex justify-content-md-center">
                                    <button 
                                        className="btn btn-light btn-lg me-md-2"
                                        onClick={() => navigate('/login')}
                                    >
                                        Get Started
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;
