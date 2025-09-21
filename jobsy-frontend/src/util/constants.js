// Backend API base URL
export const BACKEND_URL = 'http://localhost:8080/api/v1.0'; 

// Job Types
export const JOB_TYPES = {
    FIXED: 'FIXED',
    HOURLY: 'HOURLY'
};

// Job Status
export const JOB_STATUS = {
    OPEN: 'OPEN',
    IN_PROGRESS: 'IN_PROGRESS',
    COMPLETED: 'COMPLETED',
    CANCELLED: 'CANCELLED'
};

// Proposal Status
export const PROPOSAL_STATUS = {
    PENDING: 'PENDING',
    ACCEPTED: 'ACCEPTED',
    REJECTED: 'REJECTED',
    WITHDRAWN: 'WITHDRAWN'
};

// Contract Status
export const CONTRACT_STATUS = {
    ACTIVE: 'ACTIVE',
    COMPLETED: 'COMPLETED',
    CANCELLED: 'CANCELLED',
    DISPUTED: 'DISPUTED'
};

// Payment Status
export const PAYMENT_STATUS = {
    PENDING: 'PENDING',
    COMPLETED: 'COMPLETED',
    FAILED: 'FAILED',
    REFUNDED: 'REFUNDED'
};

// User Roles
export const USER_ROLES = {
    FREELANCER: 'FREELANCER',
    HIRER: 'HIRER'
};

// Categories for jobs
export const JOB_CATEGORIES = [
    'Web Development',
    'Mobile Development',
    'Data Science',
    'Machine Learning',
    'UI/UX Design',
    'Graphic Design',
    'Digital Marketing',
    'Content Writing',
    'Translation',
    'Video Editing',
    'Photography',
    'Business Consulting'
];

// Skills list
export const SKILLS = [
    'JavaScript', 'Python', 'Java', 'React', 'Node.js', 'Angular', 'Vue.js',
    'HTML/CSS', 'PHP', 'C++', 'C#', '.NET', 'Swift', 'Kotlin', 'Flutter',
    'React Native', 'MongoDB', 'MySQL', 'PostgreSQL', 'AWS', 'Azure',
    'Docker', 'Kubernetes', 'Git', 'Figma', 'Adobe Photoshop', 'Adobe Illustrator',
    'SEO', 'Google Ads', 'Facebook Ads', 'Content Writing', 'Copywriting',
    'Video Editing', 'Animation', '3D Modeling'
];