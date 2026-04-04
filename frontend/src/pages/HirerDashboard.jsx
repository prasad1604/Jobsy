import { useContext, useEffect, useState } from "react";
import { AppContext } from "../context/context";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";
import { useNavigate } from "react-router-dom";

const HirerDashboard = () => {

  const { user } = useContext(AppContext);
  const navigate = useNavigate();

  const [gigs, setGigs] = useState([]);
  const [filteredGigs, setFilteredGigs] = useState([]);
  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [price, setPrice] = useState("");

  const fetchGigs = async () => {
    try {
      const res = await axiosConfig.get(API_ENDPOINTS.GIGS);
      setGigs(res.data);
      setFilteredGigs(res.data);
    } catch (err) {
      console.error("Error fetching gigs:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGigs();
  }, []);

  useEffect(() => {
    let filtered = gigs;

    if (search) {
      filtered = filtered.filter(g =>
        g.title.toLowerCase().includes(search.toLowerCase())
      );
    }

    if (category) {
      filtered = filtered.filter(g =>
        g.category.toLowerCase() === category.toLowerCase()
      );
    }

    if (price) {
      filtered = filtered.filter(g => g.price <= price);
    }

    setFilteredGigs(filtered);

  }, [search, category, price, gigs]);

  // Only check if user exists (do not block by role)
  if (!user) {
    return (
      <div className="min-h-screen pt-28 flex items-center justify-center text-gray-500">
        Loading dashboard...
      </div>
    );
  }

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc]">

      {/* HEADER */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold text-gray-900">
          Hire Freelancers 🚀
        </h1>

        <p className="text-gray-500 mt-2">
          Browse gigs and hire the perfect freelancer.
        </p>
      </div>

      {/* FILTER BAR */}
      <div className="bg-white p-6 rounded-xl shadow-sm border border-purple-100 mb-10">

        <div className="grid md:grid-cols-4 gap-4">

          {/* SEARCH */}
          <input
            type="text"
            placeholder="Search gigs..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:border-purple-500"
          />

          {/* CATEGORY */}
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className="border border-gray-300 rounded-lg px-4 py-2"
          >
            <option value="">All Categories</option>
            <option value="Design">Design</option>
            <option value="Programming">Programming</option>
            <option value="Writing">Writing</option>
            <option value="Marketing">Marketing</option>
          </select>

          {/* PRICE */}
          <select
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            className="border border-gray-300 rounded-lg px-4 py-2"
          >
            <option value="">Any Price</option>
            <option value="500">Below ₹500</option>
            <option value="1000">Below ₹1000</option>
            <option value="5000">Below ₹5000</option>
          </select>

          {/* RESET */}
          <button
            onClick={() => {
              setSearch("");
              setCategory("");
              setPrice("");
            }}
            className="bg-purple-600 text-white rounded-lg hover:bg-purple-700"
          >
            Reset Filters
          </button>

        </div>

      </div>

      {/* GIG GRID */}

      {loading ? (
        <p className="text-gray-500">Loading gigs...</p>
      ) : filteredGigs.length === 0 ? (
        <p className="text-gray-500">No gigs found.</p>
      ) : (

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">

          {filteredGigs.map((gig) => (

            <div
              key={gig.id}
              className="bg-white rounded-xl shadow-sm border border-purple-100 hover:shadow-lg transition overflow-hidden"
            >

              {/* IMAGE */}
              <div className="h-40 bg-purple-100 flex items-center justify-center text-purple-500 font-semibold">
                Gig Preview
              </div>

              {/* CONTENT */}
              <div className="p-5">

                <div className="flex items-center gap-3 mb-3">
                  <img
                    src={gig.freelancerProfileImage}
                    alt=""
                    className="w-8 h-8 rounded-full object-cover"
                  />
                  <span className="text-sm text-gray-600">
                    {gig.freelancerName}
                  </span>
                </div>

                <h3 className="font-semibold text-gray-800 line-clamp-2">
                  {gig.title}
                </h3>

                <p className="text-sm text-gray-500 mt-2">
                  {gig.category}
                </p>

                <div className="flex justify-between items-center mt-5">
                  <span className="text-xs text-gray-400">
                    {gig.deliveryDays} days delivery
                  </span>

                  <span className="font-bold text-purple-600">
                    ₹{gig.price}
                  </span>
                </div>

                <button
                  onClick={() => navigate(`/gig/${gig.id}`)}
                  className="w-full mt-4 bg-purple-600 hover:bg-purple-700 text-white py-2 rounded-lg"
                >
                  View Gig
                </button>

              </div>

            </div>

          ))}

        </div>

      )}

    </div>
  );
};

export default HirerDashboard;