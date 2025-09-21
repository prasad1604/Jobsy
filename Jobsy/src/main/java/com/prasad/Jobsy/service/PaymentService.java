package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Payment;
import com.prasad.Jobsy.entity.UserEntity;

import java.util.List;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(Long id);
    Payment getPaymentByPaymentId(String paymentId);
    Payment updatePayment(Long id, Payment payment);
    void deletePayment(Long id);
    List<Payment> getPaymentsByContract(Contract contract);
    List<Payment> getPaymentsByPayer(UserEntity payer);
    List<Payment> getPaymentsByPayee(UserEntity payee);
    Payment updatePaymentStatus(Long id, Payment.PaymentStatus status);
    List<Payment> getPaymentsByStatus(Payment.PaymentStatus status);
    Payment getPaymentByTransactionId(String transactionId);
}
