import './App.css';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Navbar from './components/navbar';
import ProtectedRoute from './components/protectedRoutes';
import FreelancerDashboard from './pages/FreelancerDashboard';
import CreateGig from "./pages/CreateGig";
import EditGig from "./pages/EditGig";

import { Toaster } from "react-hot-toast";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { useContext } from "react";
import { AppContext } from "./context/context";

const Root = () => {
  const token = localStorage.getItem("token");

  return token
    ? <Navigate to="/dashboard" replace />
    : <Navigate to="/home" replace />;
};

const DashboardRedirect = () => {
  const { user } = useContext(AppContext);

  if (!user) return null;

  if (user.activeRole === "FREELANCER") {
    return <Navigate to="/freelancer_dashboard" replace />;
  }

  if (user.activeRole === "CLIENT") {
    return <div className="pt-28 px-10 text-white">Client Dashboard (Coming Soon)</div>;
  }

  return null;
};

const App = () => {
  return (
    <>
      <Toaster />

      <BrowserRouter>
        <Navbar />

        <Routes>

          {/* Root */}
          <Route path="/" element={<Root />} />

          {/* Public routes */}
          <Route path="/home" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          {/* Protected routes */}
          <Route element={<ProtectedRoute />}>

            {/* Main dashboard route (redirects based on role) */}
            <Route path="/dashboard" element={<DashboardRedirect />} />

            {/* Freelancer dashboard route */}
            <Route path="/freelancer_dashboard" element={<FreelancerDashboard />} />

            {/*Create gig route */}
            <Route path="/create-gig" element={<CreateGig />} />

            {/*Edit gig route */}
            <Route path="/edit-gig/:gigId" element={<EditGig />} />

          </Route>

        </Routes>
      </BrowserRouter>
    </>
  );
};

export default App;