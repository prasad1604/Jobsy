import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";

const CreateGig = () => {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    price: "",
    deliveryDays: "",
    category: ""
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await axiosConfig.post(API_ENDPOINTS.GIGS, formData);

      navigate("/freelancer_dashboard");

    } catch (error) {
      console.error("Gig creation failed:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc] text-gray-800">

      <div className="max-w-3xl mx-auto bg-white p-10 rounded-xl shadow-sm border border-purple-100">

        <h2 className="text-2xl font-bold mb-8 text-gray-900">
          Create New Gig
        </h2>

        <form onSubmit={handleSubmit} className="space-y-6">

          {/* TITLE */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Gig Title
            </label>

            <input
              type="text"
              name="title"
              required
              value={formData.title}
              onChange={handleChange}
              className="w-full border border-purple-100 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            />
          </div>

          {/* DESCRIPTION */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Description
            </label>

            <textarea
              name="description"
              required
              rows="4"
              value={formData.description}
              onChange={handleChange}
              className="w-full border border-purple-100 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            />
          </div>

          {/* PRICE */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Price (₹)
            </label>

            <input
              type="number"
              name="price"
              required
              value={formData.price}
              onChange={handleChange}
              className="w-full border border-purple-100 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            />
          </div>

          {/* DELIVERY DAYS */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Delivery Days
            </label>

            <input
              type="number"
              name="deliveryDays"
              required
              value={formData.deliveryDays}
              onChange={handleChange}
              className="w-full border border-purple-100 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            />
          </div>

          {/* CATEGORY */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Category
            </label>

            <input
              type="text"
              name="category"
              required
              value={formData.category}
              onChange={handleChange}
              className="w-full border border-purple-100 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
            />
          </div>

          {/* BUTTON */}
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-purple-600 hover:bg-purple-700 text-white py-3 rounded-lg font-medium transition"
          >
            {loading ? "Creating Gig..." : "Create Gig"}
          </button>

        </form>

      </div>
    </div>
  );
};

export default CreateGig;