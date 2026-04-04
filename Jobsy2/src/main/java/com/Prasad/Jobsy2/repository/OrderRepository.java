package com.Prasad.Jobsy2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Prasad.Jobsy2.entity.OrderEntity;
import com.Prasad.Jobsy2.entity.OrderStatus;
import com.Prasad.Jobsy2.entity.ProfileEntity;
import com.Prasad.Jobsy2.entity.GigEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    // 🔹 Get all orders of a hirer
    List<OrderEntity> findByHirer(ProfileEntity hirer);

    // 🔹 Get all orders of a freelancer
    List<OrderEntity> findByFreelancer(ProfileEntity freelancer);

    // 🔹 Get orders by hirer + status
    List<OrderEntity> findByHirerAndStatus(ProfileEntity hirer, OrderStatus status);

    // 🔹 Get orders by freelancer + status
    List<OrderEntity> findByFreelancerAndStatus(ProfileEntity freelancer, OrderStatus status);

    // 🔹 Get orders for a gig
    List<OrderEntity> findByGig(GigEntity gig);

    // 🔹 Latest orders (dashboard)
    List<OrderEntity> findTop10ByOrderByCreatedAtDesc();

    // 🔹 Count completed orders (for stats)
    long countByFreelancerAndStatus(ProfileEntity freelancer, OrderStatus status);
}