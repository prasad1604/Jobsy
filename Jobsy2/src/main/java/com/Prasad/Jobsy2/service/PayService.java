package com.Prasad.Jobsy2.service;

import com.Prasad.Jobsy2.dto.PayDTO;
import com.Prasad.Jobsy2.entity.*;
import com.Prasad.Jobsy2.repository.OrderRepository;
import com.Prasad.Jobsy2.repository.PayRepository;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;
    private final OrderRepository orderRepository;
    private final ProfileService profileService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    // 🔥 CREATE STRIPE PAYMENT INTENT (FIXED)
    public String createPaymentIntent(Long orderId) throws Exception {

        Stripe.apiKey = stripeSecretKey;

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 💰 Convert dollars → cents
        Long amountInCents = order.getAmount()
                .multiply(java.math.BigDecimal.valueOf(100))
                .longValue();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("usd") // ✅ FIXED
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return intent.getClientSecret();
    }

    // 💳 CONFIRM PAYMENT → MOVE TO ESCROW
    public PayDTO makePayment(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (payRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException("Payment already exists for this order");
        }

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Order already paid");
        }

        ProfileEntity currentUser = profileService.getCurrentProfile();

        Double amount = order.getAmount().doubleValue();
        Double platformFee = amount * 0.1;
        Double freelancerAmount = amount - platformFee;

        PayEntity payment = PayEntity.builder()
                .order(order)
                .payerId(currentUser.getId())
                .amount(amount)
                .platformFee(platformFee)
                .freelancerAmount(freelancerAmount)
                .status(PaymentStatus.ESCROW)
                .method("STRIPE")
                .transactionId("STRIPE_" + System.currentTimeMillis())
                .build();

        payment = payRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.ESCROW);
        order.setStatus(OrderStatus.IN_PROGRESS);

        orderRepository.save(order);

        return toDTO(payment);
    }

    // ✅ RELEASE PAYMENT
    public PayDTO releasePayment(Long orderId) {

        PayEntity payment = payRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.ESCROW) {
            throw new RuntimeException("Payment not in escrow");
        }

        OrderEntity order = payment.getOrder();

        payment.setStatus(PaymentStatus.PAID);
        payment = payRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);

        return toDTO(payment);
    }

    // ❌ REFUND
    public PayDTO refundPayment(Long orderId) {

        PayEntity payment = payRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.ESCROW) {
            throw new RuntimeException("Cannot refund at this stage");
        }

        OrderEntity order = payment.getOrder();

        payment.setStatus(PaymentStatus.REFUNDED);
        payment = payRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return toDTO(payment);
    }

    public PayDTO getPaymentByOrder(Long orderId) {
        PayEntity payment = payRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return toDTO(payment);
    }

    public PayDTO toDTO(PayEntity payment) {
        return PayDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .payerId(payment.getPayerId())
                .amount(payment.getAmount())
                .platformFee(payment.getPlatformFee())
                .freelancerAmount(payment.getFreelancerAmount())
                .status(payment.getStatus().name())
                .method(payment.getMethod())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}