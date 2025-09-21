package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Payment;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @Override
    public Payment getPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @Override
    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment existingPayment = getPaymentById(id);
        
        existingPayment.setAmount(updatedPayment.getAmount());
        existingPayment.setType(updatedPayment.getType());
        
        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepository.delete(payment);
    }

    @Override
    public List<Payment> getPaymentsByContract(Contract contract) {
        return paymentRepository.findByContract(contract);
    }

    @Override
    public List<Payment> getPaymentsByPayer(UserEntity payer) {
        return paymentRepository.findByPayer(payer);
    }

    @Override
    public List<Payment> getPaymentsByPayee(UserEntity payee) {
        return paymentRepository.findByPayee(payee);
    }

    @Override
    public Payment updatePaymentStatus(Long id, Payment.PaymentStatus status) {
        Payment payment = getPaymentById(id);
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found with transaction ID"));
    }
}
