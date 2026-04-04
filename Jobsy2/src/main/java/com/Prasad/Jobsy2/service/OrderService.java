package com.Prasad.Jobsy2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Prasad.Jobsy2.dto.OrderDTO;
import com.Prasad.Jobsy2.entity.*;
import com.Prasad.Jobsy2.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final GigRepository gigRepository; // ✅ FIX 1
    private final ProfileService profileService;
    private final FreelancerProfileRepository freelancerRepository; // ✅ FIX 2
    private final EmailService emailService;

    // 🟢 Create Order
    public OrderDTO createOrder(Long gigId, String requirements) {

        ProfileEntity hirer = profileService.getCurrentProfile();

        // ✅ FIXED: fetch real entity from DB
        GigEntity gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("Gig not found"));

        ProfileEntity freelancer = gig.getFreelancer();

        BigDecimal price = BigDecimal.valueOf(gig.getPrice());

        OrderEntity order = OrderEntity.builder()
                .gig(gig)
                .hirer(hirer)
                .freelancer(freelancer)
                .amount(price)
                .platformFee(price.multiply(BigDecimal.valueOf(0.1)))
                .requirements(requirements)
                .deadline(LocalDateTime.now().plusDays(gig.getDeliveryDays()))
                .build();

        order = orderRepository.save(order);

        return toDTO(order);
    }

    // 💳 Make Payment
    public OrderDTO makePayment(Long orderId) {

        ProfileEntity user = profileService.getCurrentProfile();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getHirer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.IN_PROGRESS);

        return toDTO(orderRepository.save(order));
    }

    // 📤 Submit Work
    public OrderDTO submitWork(Long orderId, String deliveryUrl) {

        ProfileEntity user = profileService.getCurrentProfile();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getFreelancer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Invalid state");
        }

        order.setDeliveryUrl(deliveryUrl);
        order.setSubmittedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.SUBMITTED);

        return toDTO(orderRepository.save(order));
    }

    // 🔁 Request Revision
    public OrderDTO requestRevision(Long orderId) {

        ProfileEntity user = profileService.getCurrentProfile();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getHirer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        order.setRevisionCount(order.getRevisionCount() + 1);
        order.setStatus(OrderStatus.IN_PROGRESS);

        return toDTO(orderRepository.save(order));
    }

    // ✅ Complete Order
    public OrderDTO completeOrder(Long orderId) {

        ProfileEntity user = profileService.getCurrentProfile();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getHirer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() != OrderStatus.SUBMITTED) {
            throw new RuntimeException("Invalid state");
        }

        order.setStatus(OrderStatus.COMPLETED);

        // ✅ FIXED: update correct freelancer earnings
        FreelancerProfileEntity freelancerProfile =
                freelancerRepository.findByUserId(order.getFreelancer().getId())
                        .orElseThrow(() -> new RuntimeException("Freelancer profile not found"));

        freelancerProfile.setTotalEarnings(
                freelancerProfile.getTotalEarnings()
                        + order.getAmount().doubleValue()
        );

        freelancerRepository.save(freelancerProfile);

        order = orderRepository.save(order);

        // 📧 Notify freelancer
        emailService.sendEmail(
                order.getFreelancer().getEmail(),
                "Order Completed",
                "Your order has been completed successfully"
        );

        return toDTO(order);
    }

    // ❌ Cancel Order
    public OrderDTO cancelOrder(Long orderId, String reason) {

        ProfileEntity user = profileService.getCurrentProfile();

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getHirer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);

        return toDTO(orderRepository.save(order));
    }

    // 📋 Get My Orders (Hirer)
    public List<OrderDTO> getMyOrders() {

        ProfileEntity user = profileService.getCurrentProfile();

        return orderRepository.findByHirer(user)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 📋 Get Freelancer Orders
    public List<OrderDTO> getFreelancerOrders() {

        ProfileEntity user = profileService.getCurrentProfile();

        return orderRepository.findByFreelancer(user)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // 🔍 Get Order By Id
    public OrderDTO getOrderById(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return toDTO(order);
    }

    // 🔄 Entity → DTO
    public OrderDTO toDTO(OrderEntity order) {

        return OrderDTO.builder()
                .id(order.getId())
                .gigTitle(order.getGig().getTitle())
                .hirerName(order.getHirer().getFullName())
                .freelancerName(order.getFreelancer().getFullName())
                .amount(order.getAmount())
                .platformFee(order.getPlatformFee())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .requirements(order.getRequirements())
                .deliveryUrl(order.getDeliveryUrl())
                .revisionCount(order.getRevisionCount())
                .submittedAt(order.getSubmittedAt())
                .deadline(order.getDeadline())
                .cancelReason(order.getCancelReason())
                .isReviewed(order.getIsReviewed())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}