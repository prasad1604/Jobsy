import { useNavigate } from "react-router-dom";

const Header = () => {
    const navigate = useNavigate();

    return (
        <div className="bg-primary text-white py-5">
            <div className="container">
                <div className="row align-items-center">
                    <div className="col-lg-6">
                        <h1 className="display-4 fw-bold mb-3">
                            Find Your Perfect Job
                        </h1>
                        <p className="lead mb-4">
                            Your one-stop solution to find your dream job and kickstart your freelancing career in tech!
                        </p>
                        <div className="d-flex gap-3">
                            <button 
                                className="btn btn-light btn-lg"
                                onClick={() => navigate('/find-work')}
                            >
                                Find Work
                            </button>
                            <button 
                                className="btn btn-outline-light btn-lg"
                                onClick={() => navigate('/find-talent')}
                            >
                                Hire Talent
                            </button>
                        </div>
                    </div>
                    <div className="col-lg-6">
                        <div className="row text-center g-4">
                            <div className="col-4">
                                <h3 className="fw-bold">10K+</h3>
                                <p>Jobs Posted</p>
                            </div>
                            <div className="col-4">
                                <h3 className="fw-bold">5K+</h3>
                                <p>Freelancers</p>
                            </div>
                            <div className="col-4">
                                <h3 className="fw-bold">98%</h3>
                                <p>Satisfaction Rate</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Header;
