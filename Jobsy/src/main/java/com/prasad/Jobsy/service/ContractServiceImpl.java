package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Override
    public Contract createContract(Contract contract) {
        // Check if contract already exists for this job
        if (contractRepository.findByJob(contract.getJob()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Contract already exists for this job");
        }
        
        contract.setContractId(UUID.randomUUID().toString());
        contract.setStatus(Contract.ContractStatus.ACTIVE);
        return contractRepository.save(contract);
    }

    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found"));
    }

    @Override
    public Contract getContractByContractId(String contractId) {
        return contractRepository.findByContractId(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found"));
    }

    @Override
    public Contract updateContract(Long id, Contract updatedContract) {
        Contract existingContract = getContractById(id);
        
        existingContract.setAgreedAmount(updatedContract.getAgreedAmount());
        existingContract.setTerms(updatedContract.getTerms());
        existingContract.setStartDate(updatedContract.getStartDate());
        existingContract.setEndDate(updatedContract.getEndDate());
        
        return contractRepository.save(existingContract);
    }

    @Override
    public void deleteContract(Long id) {
        Contract contract = getContractById(id);
        contractRepository.delete(contract);
    }

    @Override
    public Contract getContractByJob(Job job) {
        return contractRepository.findByJob(job)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found for this job"));
    }

    @Override
    public List<Contract> getContractsByFreelancer(UserEntity freelancer) {
        return contractRepository.findByFreelancer(freelancer);
    }

    @Override
    public List<Contract> getContractsByHirer(UserEntity hirer) {
        return contractRepository.findByHirer(hirer);
    }

    @Override
    public Contract updateContractStatus(Long id, Contract.ContractStatus status) {
        Contract contract = getContractById(id);
        contract.setStatus(status);
        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> getContractsByStatus(Contract.ContractStatus status) {
        return contractRepository.findByStatus(status);
    }
}
