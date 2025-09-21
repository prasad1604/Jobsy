package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.Proposal;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.JobService;
import com.prasad.Jobsy.service.ProposalService;
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
@RequestMapping("/api/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;
    private final JobService jobService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proposal createProposal(
            @Valid @RequestBody Proposal proposal,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity freelancer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        proposal.setFreelancer(freelancer);
        return proposalService.createProposal(proposal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proposal> getProposal(@PathVariable Long id) {
        Proposal proposal = proposalService.getProposalById(id);
        return ResponseEntity.ok(proposal);
    }

    @GetMapping("/proposal-id/{proposalId}")
    public ResponseEntity<Proposal> getProposalByProposalId(@PathVariable String proposalId) {
        Proposal proposal = proposalService.getProposalByProposalId(proposalId);
        return ResponseEntity.ok(proposal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proposal> updateProposal(
            @PathVariable Long id,
            @Valid @RequestBody Proposal proposal,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Proposal existingProposal = proposalService.getProposalById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!existingProposal.getFreelancer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own proposals");
        }
        
        Proposal updatedProposal = proposalService.updateProposal(id, proposal);
        return ResponseEntity.ok(updatedProposal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposal(
            @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Proposal proposal = proposalService.getProposalById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!proposal.getFreelancer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own proposals");
        }
        
        proposalService.deleteProposal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Proposal>> getProposalsForJob(@PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        List<Proposal> proposals = proposalService.getProposalsByJob(job);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/my-proposals")
    public ResponseEntity<List<Proposal>> getMyProposals(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity freelancer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Proposal> proposals = proposalService.getProposalsByFreelancer(freelancer);
        return ResponseEntity.ok(proposals);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Proposal> updateProposalStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Proposal proposal = proposalService.getProposalById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Only job hirer can update proposal status
        if (!proposal.getJob().getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the job hirer can update proposal status");
        }
        
        Proposal.ProposalStatus status = Proposal.ProposalStatus.valueOf(request.get("status"));
        Proposal updatedProposal = proposalService.updateProposalStatus(id, status);
        return ResponseEntity.ok(updatedProposal);
    }

    @GetMapping("/check-application/{jobId}")
    public ResponseEntity<Map<String, Boolean>> checkIfApplied(
            @PathVariable Long jobId,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Job job = jobService.getJobById(jobId);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        boolean hasApplied = proposalService.hasUserApplied(job, user);
        return ResponseEntity.ok(Map.of("hasApplied", hasApplied));
    }
}
