import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppContext } from '../context/AppContext';
import { JOB_CATEGORIES, JOB_TYPES } from '../util/constants';
import axios from 'axios';
import { toast } from 'react-toastify';

const FindWork = () => {
    const navigate = useNavigate();
    const { backendURL, isLoggedIn } = useContext(AppContext);
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchFilters, setSearchFilters] = useState({
        keyword: '',
        category: '',
        jobType: '',
        minBudget: '',
        maxBudget: '',
        page: 0,
        size: 10
    });

    useEffect(() => {
        fetchJobs();
    }, []);

    const fetchJobs = async (filters = searchFilters) => {
        try {
            setLoading(true);
            let url = `${backendURL}/api/jobs?page=${filters.page}&size=${filters.size}&sort=createdAt&direction=desc`;

            if (filters.keyword) {
                url = `${backendURL}/api/jobs/search?keyword=${encodeURIComponent(filters.keyword)}&page=${filters.page}&size=${filters.size}`;
            } else if (filters.category) {
                url = `${backendURL}/api/jobs/category/${encodeURIComponent(filters.category)}?page=${filters.page}&size=${filters.size}`;
            }

            const response = await axios.get(url);
            if (response.status === 200) {
                setJobs(response.data.content || response.data);
            }
        } catch (error) {
            toast.error('Failed to fetch jobs');
            console.error('Error fetching jobs:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchJobs(searchFilters);
    };

    const formatBudget = (amount) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    };

    const getTimeAgo = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diffInSeconds = Math.floor((now - date) / 1000);

        if (diffInSeconds < 60) return 'Just now';
        if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} minutes ago`;
        if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} hours ago`;
        return `${Math.floor(diffInSeconds / 86400)} days ago`;
    };

    const JobCard = ({ job }) => (
        <div className="card h-100 shadow-sm">
            <div className="card-body">
                <div className="d-flex justify-content-between align-items-start mb-2">
                    <h5 className="card-title">{job.title}</h5>
                    <span className="badge bg-primary">{job.category}</span>
                </div>
                
                <p className="card-text text-muted">
                    {job.description?.substring(0, 200)}...
                </p>
                
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <strong className="text-success">
                            {job.jobType === JOB_TYPES.FIXED ? 
                                formatBudget(job.budget) : 
                                `${formatBudget(job.budget)}/hr`
                            }
                        </strong>
                        <span className="text-muted ms-2">
                            {job.jobType === JOB_TYPES.FIXED ? 'Fixed Price' : 'Hourly'}
                        </span>
                    </div>
                    <small className="text-muted">{getTimeAgo(job.createdAt)}</small>
                </div>
                
                <div className="d-flex flex-wrap gap-1 mb-3">
                    {job.skills?.slice(0, 3).map((skill, index) => (
                        <span key={index} className="badge bg-light text-dark">
                            {skill}
                        </span>
                    ))}
                    {job.skills?.length > 3 && (
                        <span className="badge bg-light text-dark">
                            +{job.skills.length - 3} more
                        </span>
                    )}
                </div>
                
                <div className="d-flex justify-content-between align-items-center">
                    <small className="text-muted">
                        Proposals: {job.proposalCount || 0}
                    </small>
                    <button 
                        className="btn btn-primary btn-sm"
                        onClick={() => navigate(`/job/${job.id}`)}
                    >
                        View Details
                    </button>
                </div>
            </div>
        </div>
    );

    return (
        <div className="container mt-4">
            <div className="row">
                <div className="col-12">
                    <div className="bg-light p-4 rounded mb-4">
                        <h2 className="text-center mb-3">Discover opportunities that match your skills</h2>
                        
                        <form onSubmit={handleSearch}>
                            <div className="row g-3">
                                <div className="col-md-4">
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Search jobs..."
                                        value={searchFilters.keyword}
                                        onChange={(e) => setSearchFilters(prev => ({
                                            ...prev,
                                            keyword: e.target.value
                                        }))}
                                    />
                                </div>
                                <div className="col-md-3">
                                    <select
                                        className="form-select"
                                        value={searchFilters.category}
                                        onChange={(e) => setSearchFilters(prev => ({
                                            ...prev,
                                            category: e.target.value
                                        }))}
                                    >
                                        <option value="">All Categories</option>
                                        {JOB_CATEGORIES.map(category => (
                                            <option key={category} value={category}>
                                                {category}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className="col-md-3">
                                    <select
                                        className="form-select"
                                        value={searchFilters.jobType}
                                        onChange={(e) => setSearchFilters(prev => ({
                                            ...prev,
                                            jobType: e.target.value
                                        }))}
                                    >
                                        <option value="">All Types</option>
                                        <option value={JOB_TYPES.FIXED}>Fixed Price</option>
                                        <option value={JOB_TYPES.HOURLY}>Hourly</option>
                                    </select>
                                </div>
                                <div className="col-md-2">
                                    <button type="submit" className="btn btn-primary w-100">
                                        Search
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            {loading ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                    <p className="mt-3">Loading jobs...</p>
                </div>
            ) : (
                <div className="row">
                    {jobs.length > 0 ? (
                        jobs.map(job => (
                            <div key={job.id} className="col-lg-6 col-xl-4 mb-4">
                                <JobCard job={job} />
                            </div>
                        ))
                    ) : (
                        <div className="col-12">
                            <div className="text-center py-5">
                                <h4>No jobs found</h4>
                                <p className="text-muted">Try adjusting your search criteria</p>
                            </div>
                        </div>
                    )}
                </div>
            )}

            {!isLoggedIn && (
                <div className="bg-light p-4 rounded mt-5 text-center">
                    <h4>Ready to get started?</h4>
                    <p className="text-muted mb-4">Join thousands of freelancers earning money on Jobsy</p>
                    <button 
                        className="btn btn-primary btn-lg me-3"
                        onClick={() => navigate('/login')}
                    >
                        Sign Up
                    </button>
                    <button 
                        className="btn btn-outline-primary btn-lg"
                        onClick={() => navigate('/login')}
                    >
                        Login
                    </button>
                </div>
            )}
        </div>
    );
};

export default FindWork;
