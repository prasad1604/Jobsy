package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.Payment;
import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByContract(Contract contract);
    List<Payment> findByPayer(UserEntity payer);
    List<Payment> findByPayee(UserEntity payee);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    Optional<Payment> findByTransactionId(String transactionId);
}
