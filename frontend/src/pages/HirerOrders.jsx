import { useEffect, useState } from "react";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";
import { loadStripe } from "@stripe/stripe-js";
import {
  Elements,
  CardElement,
  useStripe,
  useElements
} from "@stripe/react-stripe-js";

const stripePromise = loadStripe("pk_test_51TIa64Rs7PkJvg10rXmlyMFDgdW8a9BRTlNOTlUUe6aZbcZeYXkJ5eZx4YsYMbYHMosNyBaN5YUR2anYemh2TOZE00EwW5sjDN");

// 🔥 PAYMENT FORM COMPONENT
const PaymentForm = ({ orderId, onSuccess }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!stripe || !elements) return;

    try {
      setLoading(true);

      const res = await axiosConfig.post(
        `${API_ENDPOINTS.PAYMENTS}/stripe/create-intent/${orderId}`
      );

      const clientSecret = res.data.clientSecret;

      const result = await stripe.confirmCardPayment(clientSecret, {
        payment_method: {
          card: elements.getElement(CardElement)
        }
      });

      if (result.error) {
        alert(result.error.message);
        return;
      }

      await axiosConfig.post(`${API_ENDPOINTS.PAYMENTS}/${orderId}`);

      alert("Payment successful 💸");
      onSuccess();

    } catch (err) {
      console.error(err);
      alert("Payment failed ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mt-3">
      <div className="border p-3 rounded">
        <CardElement />
      </div>

      <button
        onClick={handleSubmit}
        disabled={loading}
        className="mt-3 w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 disabled:bg-gray-400"
      >
        {loading ? "Processing..." : "Confirm Payment"}
      </button>
    </div>
  );
};

// 🔥 MAIN COMPONENT
const HirerOrders = () => {

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [payingOrder, setPayingOrder] = useState(null);
  const [actionLoading, setActionLoading] = useState(null); // ✅ restored

  const fetchOrders = async () => {
    try {
      const res = await axiosConfig.get(`${API_ENDPOINTS.ORDERS}/my`);
      setOrders(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  return (
    <div className="min-h-screen pt-28 px-12 bg-[#f8f7fc]">

      <h1 className="text-3xl font-bold mb-6">My Orders 📦</h1>

      {loading ? (
        <p>Loading...</p>
      ) : (

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">

          {orders.map((order) => {

            const isPaid = order.paymentStatus === "PAID";
            const isRefunded = order.paymentStatus === "REFUNDED";
            const isProcessing = actionLoading === order.id;

            return (
              <div key={order.id} className="bg-white p-5 rounded shadow">

                <h3 className="font-semibold">{order.gigTitle}</h3>

                <p className="text-sm text-gray-500 mt-1">
                  Freelancer: {order.freelancerName}
                </p>

                <div className="mt-3 font-bold text-purple-600">
                  ${order.amount}
                </div>

                {/* 💳 PAYMENT */}
                {isPaid ? (
                  <button
                    disabled
                    className="mt-3 w-full bg-gray-300 py-2 rounded"
                  >
                    Paid
                  </button>
                ) : isRefunded ? (
                  <button
                    disabled
                    className="mt-3 w-full bg-red-100 text-red-500 py-2 rounded"
                  >
                    Refunded
                  </button>
                ) : (
                  <>
                    <button
                      onClick={() => setPayingOrder(order.id)}
                      className="mt-3 w-full bg-green-600 text-white py-2 rounded"
                    >
                      Pay
                    </button>

                    {payingOrder === order.id && (
                      <Elements stripe={stripePromise}>
                        <PaymentForm
                          orderId={order.id}
                          onSuccess={() => {
                            setPayingOrder(null);
                            fetchOrders();
                          }}
                        />
                      </Elements>
                    )}
                  </>
                )}

                {/* 📦 REQUIREMENTS */}
                {order.requirements && (
                  <p className="text-sm text-gray-500 mt-3">
                    {order.requirements}
                  </p>
                )}

                {/* 🔗 DELIVERY */}
                {order.deliveryUrl && (
                  <a
                    href={order.deliveryUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="block mt-3 text-blue-600 text-sm underline"
                  >
                    View Delivery
                  </a>
                )}

                {/* ✅ ACCEPT / ❌ REJECT (RESTORED) */}
                {order.status === "SUBMITTED" && (
                  <div className="mt-4 flex gap-2">

                    <button
                      onClick={async () => {
                        try {
                          setActionLoading(order.id);

                          if (order.paymentStatus === "PENDING") {
                            setPayingOrder(order.id);
                            return;
                          }

                          await axiosConfig.post(
                            `${API_ENDPOINTS.PAYMENTS}/${order.id}/release`
                          );

                          fetchOrders();

                        } catch (err) {
                          console.error(err);
                        } finally {
                          setActionLoading(null);
                        }
                      }}
                      disabled={isProcessing}
                      className="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 disabled:bg-gray-300"
                    >
                      {isProcessing ? "Processing..." : "Accept"}
                    </button>

                    <button
                      onClick={async () => {
                        try {
                          setActionLoading(order.id);

                          await axiosConfig.post(
                            `${API_ENDPOINTS.ORDERS}/${order.id}/revision`
                          );

                          fetchOrders();

                        } catch (err) {
                          console.error(err);
                        } finally {
                          setActionLoading(null);
                        }
                      }}
                      disabled={isProcessing}
                      className="w-full bg-red-600 text-white py-2 rounded-lg hover:bg-red-700 disabled:bg-gray-300"
                    >
                      {isProcessing ? "Processing..." : "Reject"}
                    </button>

                  </div>
                )}

              </div>
            );
          })}

        </div>

      )}
    </div>
  );
};

export default HirerOrders;