package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByContractId(String contractId);
    Optional<Contract> findByJob(Job job);
    List<Contract> findByFreelancer(UserEntity freelancer);
    List<Contract> findByHirer(UserEntity hirer);
    List<Contract> findByStatus(Contract.ContractStatus status);
}
