import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";

const ViewGig = () => {

  const { id } = useParams();
  const navigate = useNavigate();

  const [gig, setGig] = useState(null);
  const [loading, setLoading] = useState(true);
  const [requirements, setRequirements] = useState("");
  const [placingOrder, setPlacingOrder] = useState(false);

  const fetchGig = async () => {
    try {
      const res = await axiosConfig.get(`${API_ENDPOINTS.GIGS}/${id}`);
      setGig(res.data);
    } catch (err) {
      console.error("Error fetching gig:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGig();
  }, [id]);

  // ✅ PLACE ORDER FUNCTION
  const placeOrder = async () => {
    try {
      setPlacingOrder(true);

      await axiosConfig.post(
        `${API_ENDPOINTS.ORDERS}/${gig.id}`,
        requirements, // ⚠️ must be plain string
        {
          headers: {
            "Content-Type": "text/plain"
          }
        }
      );

      alert("Order placed successfully!");

      // redirect to orders page
      navigate("/order/${gig.id}");

    } catch (err) {
      console.error("Order failed:", err);
      alert("Failed to place order");
    } finally {
      setPlacingOrder(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen pt-28 flex justify-center items-center text-gray-500">
        Loading gig...
      </div>
    );
  }

  if (!gig) {
    return (
      <div className="min-h-screen pt-28 flex justify-center items-center text-gray-500">
        Gig not found.
      </div>
    );
  }

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc]">

      {/* BACK BUTTON */}
      <button
        onClick={() => navigate(-1)}
        className="mb-6 text-purple-600 hover:underline"
      >
        ← Back
      </button>

      <div className="grid md:grid-cols-2 gap-10">

        {/* LEFT - IMAGE */}
        <div className="bg-white rounded-xl shadow-sm border border-purple-100 p-6">
          <div className="h-64 bg-purple-100 flex items-center justify-center text-purple-500 font-semibold rounded-lg">
            Gig Preview
          </div>
        </div>

        {/* RIGHT - DETAILS */}
        <div className="bg-white rounded-xl shadow-sm border border-purple-100 p-6">

          {/* FREELANCER */}
          <div className="flex items-center gap-3 mb-4">
            <img
              src={gig.freelancerProfileImage}
              alt=""
              className="w-10 h-10 rounded-full object-cover"
            />
            <span className="text-gray-700 font-medium">
              {gig.freelancerName}
            </span>
          </div>

          {/* TITLE */}
          <h1 className="text-2xl font-bold text-gray-900">
            {gig.title}
          </h1>

          {/* CATEGORY */}
          <p className="text-gray-500 mt-2">
            {gig.category}
          </p>

          {/* DESCRIPTION */}
          <div className="mt-6">
            <h3 className="font-semibold text-gray-800 mb-2">
              Description
            </h3>
            <p className="text-gray-600">
              {gig.description}
            </p>
          </div>

          {/* DELIVERY */}
          <div className="mt-6 text-sm text-gray-500">
            Delivery Time: {gig.deliveryDays} days
          </div>

          {/* PRICE */}
          <div className="mt-6 text-2xl font-bold text-purple-600">
            ₹{gig.price}
          </div>

          {/* REQUIREMENTS INPUT (NEW) */}
          <textarea
            placeholder="Enter your requirements (optional)..."
            value={requirements}
            onChange={(e) => setRequirements(e.target.value)}
            className="w-full mt-6 p-3 border rounded-lg"
          />

          {/* PLACE ORDER BUTTON (FIXED) */}
          <button
            onClick={placeOrder}
            disabled={placingOrder}
            className="w-full mt-6 bg-purple-600 hover:bg-purple-700 text-white py-3 rounded-lg disabled:opacity-50"
          >
            {placingOrder ? "Placing Order..." : "Place Order"}
          </button>

        </div>

      </div>

    </div>
  );
};

export default ViewGig;