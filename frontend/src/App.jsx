import './App.css';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Navbar from './components/navbar';
import ProtectedRoute from './components/protectedRoutes';
import FreelancerDashboard from './pages/FreelancerDashboard';
import CreateGig from "./pages/CreateGig";
import EditGig from "./pages/EditGig";
import HirerDashboard from "./pages/HirerDashboard";
import ViewGig from './pages/ViewGig';
import HirerOrders from './pages/HirerOrders';
import FreelancerOrders from './pages/FreelancerOrders';

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

  if (!user) {
    return (
      <div className="pt-28 text-center text-gray-600">
        Loading dashboard...
      </div>
    );
  }

  if (user.activeRole?.toUpperCase() === "FREELANCER") {
    return <Navigate to="/freelancer_dashboard" replace />;
  }

  if (user.activeRole?.toUpperCase() === "HIRER") {
    return <Navigate to="/hirer-dashboard" replace />;
  }

  return (
    <div className="pt-28 text-center text-gray-600">
      No role selected
    </div>
  );
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

            {/*Hirer dashboard route */}
            <Route path="/hirer-dashboard" element={<HirerDashboard />} />

            <Route path="/gig/:id" element={<ViewGig />} />

            <Route path="/order/:gigId" element={<HirerOrders />} />

            <Route path="/freelancer-orders" element={<FreelancerOrders />} />

          </Route>

        </Routes>
      </BrowserRouter>
    </>
  );
};

export default App;