package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Payment;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.ContractService;
import com.prasad.Jobsy.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ContractService contractService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment createPayment(
            @Valid @RequestBody Payment payment,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity payer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Verify the payer is the hirer of the contract
        if (!payment.getContract().getHirer().equals(payer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only create payments for your own contracts");
        }
        
        payment.setPayer(payer);
        payment.setPayee(payment.getContract().getFreelancer());
        return paymentService.createPayment(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/payment-id/{paymentId}")
    public ResponseEntity<Payment> getPaymentByPaymentId(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody Payment payment,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Payment existingPayment = paymentService.getPaymentById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!existingPayment.getPayer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update payments you created");
        }
        
        Payment updatedPayment = paymentService.updatePayment(id, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Payment payment = paymentService.getPaymentById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!payment.getPayer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete payments you created");
        }
        
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<Payment>> getPaymentsByContract(@PathVariable Long contractId) {
        Contract contract = contractService.getContractById(contractId);
        List<Payment> payments = paymentService.getPaymentsByContract(contract);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/my-payments")
    public ResponseEntity<List<Payment>> getMyPayments(
            @CurrentSecurityContext(expression = "authentication?.name") String email,
            @RequestParam(defaultValue = "all") String type) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Payment> payments;
        if ("sent".equals(type)) {
            payments = paymentService.getPaymentsByPayer(user);
        } else if ("received".equals(type)) {
            payments = paymentService.getPaymentsByPayee(user);
        } else {
            // Return both sent and received payments
            List<Payment> sentPayments = paymentService.getPaymentsByPayer(user);
            List<Payment> receivedPayments = paymentService.getPaymentsByPayee(user);
            sentPayments.addAll(receivedPayments);
            payments = sentPayments;
        }
        
        return ResponseEntity.ok(payments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Payment payment = paymentService.getPaymentById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Only payer can update payment status
        if (!payment.getPayer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update payments you created");
        }
        
        Payment.PaymentStatus status = Payment.PaymentStatus.valueOf(request.get("status"));
        Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }
}
