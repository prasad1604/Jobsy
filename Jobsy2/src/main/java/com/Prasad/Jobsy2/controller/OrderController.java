package com.Prasad.Jobsy2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Prasad.Jobsy2.dto.OrderDTO;
import com.Prasad.Jobsy2.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 🟢 Create Order
    @PostMapping("/{gigId}")
    public ResponseEntity<?> createOrder(
            @PathVariable Long gigId,
            @RequestBody(required = false) String requirements) {

        try {
            OrderDTO order = orderService.createOrder(gigId, requirements);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // 💳 Make Payment
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<?> makePayment(@PathVariable Long orderId) {

        try {
            OrderDTO order = orderService.makePayment(orderId);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // 📤 Submit Work (Freelancer)
    @PostMapping("/{orderId}/submit")
    public ResponseEntity<?> submitWork(
            @PathVariable Long orderId,
            @RequestBody String deliveryUrl) {

        try {
            OrderDTO order = orderService.submitWork(orderId, deliveryUrl);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // 🔁 Request Revision (Hirer)
    @PostMapping("/{orderId}/revision")
    public ResponseEntity<?> requestRevision(@PathVariable Long orderId) {

        try {
            OrderDTO order = orderService.requestRevision(orderId);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // ✅ Complete Order
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId) {

        try {
            OrderDTO order = orderService.completeOrder(orderId);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // ❌ Cancel Order
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody String reason) {

        try {
            OrderDTO order = orderService.cancelOrder(orderId, reason);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // 📋 Get Hirer Orders
    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {

        List<OrderDTO> orders = orderService.getMyOrders();

        return ResponseEntity.ok(orders);
    }

    // 📋 Get Freelancer Orders
    @GetMapping("/freelancer")
    public ResponseEntity<List<OrderDTO>> getFreelancerOrders() {

        List<OrderDTO> orders = orderService.getFreelancerOrders();

        return ResponseEntity.ok(orders);
    }

    // 🔍 Get Order By Id
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {

        try {
            OrderDTO order = orderService.getOrderById(orderId);

            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}