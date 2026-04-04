package com.Prasad.Jobsy2.controller;

import com.Prasad.Jobsy2.dto.PayDTO;
import com.Prasad.Jobsy2.service.PayService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    // 🔥 CREATE STRIPE PAYMENT INTENT
    @PostMapping("/stripe/create-intent/{orderId}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable Long orderId) {
        try {
            String clientSecret = payService.createPaymentIntent(orderId);
            return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 💳 CONFIRM PAYMENT → MOVE TO ESCROW
    @PostMapping("/{orderId}")
    public ResponseEntity<?> makePayment(@PathVariable Long orderId) {
        try {
            PayDTO payment = payService.makePayment(orderId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ RELEASE PAYMENT (Accept Order)
    @PostMapping("/{orderId}/release")
    public ResponseEntity<?> releasePayment(@PathVariable Long orderId) {
        try {
            PayDTO payment = payService.releasePayment(orderId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ❌ REFUND PAYMENT (Cancel Order)
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable Long orderId) {
        try {
            PayDTO payment = payService.refundPayment(orderId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔍 GET PAYMENT DETAILS
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getPayment(@PathVariable Long orderId) {
        try {
            PayDTO payment = payService.getPaymentByOrder(orderId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}