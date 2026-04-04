package com.Prasad.Jobsy2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Prasad.Jobsy2.entity.PayEntity;

import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<PayEntity, Long> {

    // 🔍 Get payment by order
    Optional<PayEntity> findByOrderId(Long orderId);

}