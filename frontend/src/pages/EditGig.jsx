import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";

const EditGig = () => {

  const { gigId } = useParams();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    price: "",
    deliveryDays: "",
    category: ""
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {

    const fetchGig = async () => {
      try {

        const res = await axiosConfig.get(`${API_ENDPOINTS.GIGS}/${gigId}`);

        setFormData({
          title: res.data.title,
          description: res.data.description,
          price: res.data.price,
          deliveryDays: res.data.deliveryDays,
          category: res.data.category
        });

      } catch (error) {
        console.error("Failed to fetch gig:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchGig();

  }, [gigId]);

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      await axiosConfig.put(
        `${API_ENDPOINTS.GIGS}/${gigId}`,
        formData
      );

      navigate("/freelancer_dashboard");

    } catch (error) {
      console.error("Update failed:", error);
    }

  };

  if (loading) {
    return (
      <div className="min-h-screen pt-28 text-center">
        Loading...
      </div>
    );
  }

  return (

    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc] text-gray-800">

      <div className="max-w-2xl mx-auto bg-white p-8 rounded-xl border border-purple-100 shadow-sm">

        <h2 className="text-2xl font-bold mb-6 text-gray-900">
          Edit Gig
        </h2>

        <form onSubmit={handleSubmit} className="space-y-5">

          {/* TITLE */}
          <div>
            <label className="block text-sm font-medium mb-1">
              Title
            </label>

            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              className="w-full border border-purple-200 rounded-lg px-4 py-2"
              required
            />
          </div>

          {/* DESCRIPTION */}
          <div>
            <label className="block text-sm font-medium mb-1">
              Description
            </label>

            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="w-full border border-purple-200 rounded-lg px-4 py-2"
              rows="4"
              required
            />
          </div>

          {/* PRICE */}
          <div>
            <label className="block text-sm font-medium mb-1">
              Price
            </label>

            <input
              type="number"
              name="price"
              value={formData.price}
              onChange={handleChange}
              className="w-full border border-purple-200 rounded-lg px-4 py-2"
              required
            />
          </div>

          {/* DELIVERY DAYS */}
          <div>
            <label className="block text-sm font-medium mb-1">
              Delivery Days
            </label>

            <input
              type="number"
              name="deliveryDays"
              value={formData.deliveryDays}
              onChange={handleChange}
              className="w-full border border-purple-200 rounded-lg px-4 py-2"
              required
            />
          </div>

          {/* CATEGORY */}
          <div>
            <label className="block text-sm font-medium mb-1">
              Category
            </label>

            <input
              type="text"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className="w-full border border-purple-200 rounded-lg px-4 py-2"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-purple-600 hover:bg-purple-700 text-white py-2 rounded-lg transition"
          >
            Update Gig
          </button>

        </form>

      </div>

    </div>
  );
};

export default EditGig;