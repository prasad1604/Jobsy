import { useEffect, useState } from "react";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";

const FreelancerOrders = () => {

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const [deliveryUrl, setDeliveryUrl] = useState({});
  const [submittingId, setSubmittingId] = useState(null);

  const fetchOrders = async () => {
    try {
      const res = await axiosConfig.get(`${API_ENDPOINTS.ORDERS}/freelancer`);
      setOrders(res.data);
    } catch (err) {
      console.error("Error fetching orders:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleSubmitWork = async (orderId) => {
    try {
      setSubmittingId(orderId);

      await axiosConfig.post(
        `${API_ENDPOINTS.ORDERS}/${orderId}/submit`,
        deliveryUrl[orderId] || ""
      );

      fetchOrders();
    } catch (err) {
      console.error("Error submitting work:", err);
    } finally {
      setSubmittingId(null);
    }
  };

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc]">

      {/* HEADER */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold text-gray-900">
          Work Orders 🛠️
        </h1>

        <p className="text-gray-500 mt-2">
          Manage and deliver your assigned work.
        </p>
      </div>

      {/* CONTENT */}
      {loading ? (
        <p className="text-gray-500">Loading orders...</p>
      ) : orders.length === 0 ? (
        <p className="text-gray-500">No orders found.</p>
      ) : (

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">

          {orders.map((order) => {

            const isSubmitting = submittingId === order.id;
            const canSubmit =
              order.status === "IN_PROGRESS" && !order.deliveryUrl;

            return (
              <div
                key={order.id}
                className="bg-white rounded-xl shadow-sm border border-purple-100 p-5 hover:shadow-lg transition"
              >

                {/* TITLE */}
                <h3 className="font-semibold text-gray-800 line-clamp-2">
                  {order.gigTitle}
                </h3>

                {/* HIRER */}
                <p className="text-sm text-gray-500 mt-2">
                  Hirer: {order.hirerName}
                </p>

                {/* STATUS */}
                <div className="mt-4 flex justify-between items-center">

                  <span className="text-xs px-3 py-1 rounded-full bg-purple-100 text-purple-600">
                    {order.status}
                  </span>

                  <span className="text-xs px-3 py-1 rounded-full bg-gray-100 text-gray-600">
                    {order.paymentStatus}
                  </span>

                </div>

                {/* PRICE */}
                <div className="mt-4 font-bold text-purple-600">
                  ₹{order.amount}
                </div>

                {/* REQUIREMENTS */}
                {order.requirements && (
                  <p className="text-sm text-gray-500 mt-3 line-clamp-2">
                    {order.requirements}
                  </p>
                )}

                {/* DELIVERY INPUT */}
                {canSubmit && (
                  <div className="mt-4">
                    <input
                      type="text"
                      placeholder="Enter delivery URL"
                      value={deliveryUrl[order.id] || ""}
                      onChange={(e) =>
                        setDeliveryUrl({
                          ...deliveryUrl,
                          [order.id]: e.target.value
                        })
                      }
                      className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"
                    />

                    <button
                      onClick={() => handleSubmitWork(order.id)}
                      disabled={isSubmitting}
                      className="w-full mt-3 bg-purple-600 hover:bg-purple-700 text-white py-2 rounded-lg disabled:bg-gray-300"
                    >
                      {isSubmitting ? "Submitting..." : "Submit Work"}
                    </button>
                  </div>
                )}

                {/* VIEW DELIVERY */}
                {order.deliveryUrl && (
                  <a
                    href={order.deliveryUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="block mt-4 text-blue-600 text-sm underline"
                  >
                    View Submitted Work
                  </a>
                )}

              </div>
            );
          })}

        </div>

      )}

    </div>
  );
};

export default FreelancerOrders;