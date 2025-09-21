import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

// Components
import Menubar from './components/Menubar';

// Pages
import Home from './pages/Home';
import Login from './pages/Login';
import EmailVerify from './pages/EmailVerify';
import ResetPassword from './pages/ResetPassword';
import Dashboard from './pages/Dashboard';
import FindWork from './pages/FindWork';

function App() {
  return (
    <div className="App">
      <Menubar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Login />} />
        <Route path="/email-verify" element={<EmailVerify />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/find-work" element={<FindWork />} />
        <Route path="/find-talent" element={<FindWork />} />
        <Route path="/my-jobs" element={<Dashboard />} />
        <Route path="/profile" element={<Dashboard />} />
        <Route path="/messages" element={<Dashboard />} />
        <Route path="*" element={
          <div className="container mt-5 text-center">
            <h1>404 - Page Not Found</h1>
            <p>The page you're looking for doesn't exist.</p>
            <button className="btn btn-primary" onClick={() => window.location.href = '/'}>
              Go Home
            </button>
          </div>
        } />
      </Routes>
      
      <ToastContainer 
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
}

export default App;
