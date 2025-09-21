package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Contract;
import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.ContractService;
import com.prasad.Jobsy.service.JobService;
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
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final JobService jobService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contract createContract(
            @Valid @RequestBody Contract contract,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity hirer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Verify the hirer owns the job
        if (!contract.getJob().getHirer().equals(hirer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only create contracts for your own jobs");
        }
        
        contract.setHirer(hirer);
        return contractService.createContract(contract);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        Contract contract = contractService.getContractById(id);
        return ResponseEntity.ok(contract);
    }

    @GetMapping("/contract-id/{contractId}")
    public ResponseEntity<Contract> getContractByContractId(@PathVariable String contractId) {
        Contract contract = contractService.getContractByContractId(contractId);
        return ResponseEntity.ok(contract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Long id,
            @Valid @RequestBody Contract contract,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Contract existingContract = contractService.getContractById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Only hirer can update contract
        if (!existingContract.getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update contracts you created");
        }
        
        Contract updatedContract = contractService.updateContract(id, contract);
        return ResponseEntity.ok(updatedContract);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Contract contract = contractService.getContractById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!contract.getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete contracts you created");
        }
        
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Contract> getContractByJob(@PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        Contract contract = contractService.getContractByJob(job);
        return ResponseEntity.ok(contract);
    }

    @GetMapping("/my-contracts")
    public ResponseEntity<List<Contract>> getMyContracts(
            @CurrentSecurityContext(expression = "authentication?.name") String email,
            @RequestParam(defaultValue = "all") String role) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Contract> contracts;
        if ("freelancer".equals(role)) {
            contracts = contractService.getContractsByFreelancer(user);
        } else if ("hirer".equals(role)) {
            contracts = contractService.getContractsByHirer(user);
        } else {
            // Return both freelancer and hirer contracts
            List<Contract> freelancerContracts = contractService.getContractsByFreelancer(user);
            List<Contract> hirerContracts = contractService.getContractsByHirer(user);
            freelancerContracts.addAll(hirerContracts);
            contracts = freelancerContracts;
        }
        
        return ResponseEntity.ok(contracts);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Contract> updateContractStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Contract contract = contractService.getContractById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Both hirer and freelancer can update status
        if (!contract.getHirer().equals(user) && !contract.getFreelancer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update contracts you're involved in");
        }
        
        Contract.ContractStatus status = Contract.ContractStatus.valueOf(request.get("status"));
        Contract updatedContract = contractService.updateContractStatus(id, status);
        return ResponseEntity.ok(updatedContract);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Contract>> getContractsByStatus(@PathVariable Contract.ContractStatus status) {
        List<Contract> contracts = contractService.getContractsByStatus(status);
        return ResponseEntity.ok(contracts);
    }
}
