package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;

import java.util.List;

public interface ContractService {
    Contract createContract(Contract contract);
    Contract getContractById(Long id);
    Contract getContractByContractId(String contractId);
    Contract updateContract(Long id, Contract contract);
    void deleteContract(Long id);
    Contract getContractByJob(Job job);
    List<Contract> getContractsByFreelancer(UserEntity freelancer);
    List<Contract> getContractsByHirer(UserEntity hirer);
    Contract updateContractStatus(Long id, Contract.ContractStatus status);
    List<Contract> getContractsByStatus(Contract.ContractStatus status);
}
