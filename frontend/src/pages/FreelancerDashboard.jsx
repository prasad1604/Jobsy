import { useContext, useEffect, useState } from "react";
import { AppContext } from "../context/context";
import { useNavigate } from "react-router-dom";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";

const FreelancerDashboard = () => {
  const { user } = useContext(AppContext);
  const navigate = useNavigate();

  const [gigs, setGigs] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchGigs = async () => {
    try {
      // fetch ALL gigs (active + inactive)
      const res = await axiosConfig.get(API_ENDPOINTS.MY_ALL_GIGS);
      setGigs(res.data);
    } catch (error) {
      console.error("Error fetching gigs:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGigs();
  }, []);

  const handleDelete = async (gigId) => {
    try {
      await axiosConfig.delete(`${API_ENDPOINTS.GIGS}/${gigId}`);
      fetchGigs();
    } catch (error) {
      console.error("Delete failed:", error);
    }
  };

  const handleRestore = async (gigId) => {
    try {
      await axiosConfig.put(`${API_ENDPOINTS.RESTORE_GIG}/${gigId}`);
      fetchGigs();
    } catch (error) {
      console.error("Restore failed:", error);
    }
  };

  const handleUpdate = (gigId) => {
    navigate(`/edit-gig/${gigId}`);
  };

  if (!user || user.activeRole !== "FREELANCER") return null;

  const totalGigs = gigs.length;
  const activeGigs = gigs.filter(g => g.isActive).length;

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc] text-gray-800">

      {/* HEADER */}
      <div className="flex justify-between items-center mb-12">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Welcome back, {user.fullName} 👋
          </h1>
          <p className="text-gray-500 mt-2">
            Here’s an overview of your freelance activity.
          </p>
        </div>

        <button
          onClick={() => navigate("/create-gig")}
          className="bg-purple-600 hover:bg-purple-700 text-white transition px-6 py-3 rounded-lg font-medium shadow-md"
        >
          + Create New Gig
        </button>
      </div>

      {/* STATS */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-14">

        <div className="bg-white p-6 rounded-xl shadow-sm border border-purple-100">
          <h3 className="text-gray-500 text-sm">Total Gigs</h3>
          <p className="text-3xl font-bold mt-2 text-purple-600">{totalGigs}</p>
        </div>

        <div className="bg-white p-6 rounded-xl shadow-sm border border-purple-100">
          <h3 className="text-gray-500 text-sm">Active Gigs</h3>
          <p className="text-3xl font-bold mt-2 text-purple-600">{activeGigs}</p>
        </div>

        <div className="bg-white p-6 rounded-xl shadow-sm border border-purple-100">
          <h3 className="text-gray-500 text-sm">Rating</h3>
          <p className="text-3xl font-bold mt-2 text-purple-600">0.0 ⭐</p>
          <p className="text-gray-400 text-xs mt-1">0 reviews</p>
        </div>

        <div className="bg-white p-6 rounded-xl shadow-sm border border-purple-100">
          <h3 className="text-gray-500 text-sm">Total Earnings</h3>
          <p className="text-3xl font-bold mt-2 text-purple-600">₹0</p>
        </div>

      </div>

      {/* MY GIGS */}
      <div className="bg-white rounded-xl p-8 shadow-sm border border-purple-100">
        <h2 className="text-xl font-semibold mb-6 text-gray-900">My Gigs</h2>

        {loading ? (
          <p className="text-gray-500">Loading...</p>
        ) : gigs.length === 0 ? (
          <div className="text-center text-gray-500 py-10">
            <p>You haven't created any gigs yet.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {gigs.map((gig) => (
              <div
                key={gig.id}
                className="flex justify-between items-center border border-purple-100 rounded-lg p-4 hover:shadow-sm transition"
              >
                <div>
                  <h3 className="font-semibold text-gray-800">
                    {gig.title}
                  </h3>
                  <p className="text-sm text-gray-500">
                    ₹{gig.price} • {gig.deliveryDays} days • {gig.category}
                  </p>
                </div>

                <div className="flex items-center gap-3">

                  <span
                    className={`text-xs px-3 py-1 rounded-full ${
                      gig.isActive
                        ? "bg-green-100 text-green-600"
                        : "bg-red-100 text-red-600"
                    }`}
                  >
                    {gig.isActive ? "Active" : "Inactive"}
                  </span>

                  <button
                    onClick={() => handleUpdate(gig.id)}
                    className="px-3 py-1 text-sm bg-blue-500 hover:bg-blue-600 text-white rounded"
                  >
                    Edit
                  </button>

                  {gig.isActive ? (
                    <button
                      onClick={() => handleDelete(gig.id)}
                      className="px-3 py-1 text-sm bg-red-500 hover:bg-red-600 text-white rounded"
                    >
                      Deactivate
                    </button>
                  ) : (
                    <button
                      onClick={() => handleRestore(gig.id)}
                      className="px-3 py-1 text-sm bg-green-500 hover:bg-green-600 text-white rounded"
                    >
                      Activate
                    </button>
                  )}

                </div>
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
};

export default FreelancerDashboard;