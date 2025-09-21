package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.Proposal;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;

    @Override
    public Proposal createProposal(Proposal proposal) {
        // Check if user already applied for this job
        if (proposalRepository.findByJobAndFreelancer(proposal.getJob(), proposal.getFreelancer()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already applied for this job");
        }
        
        proposal.setProposalId(UUID.randomUUID().toString());
        proposal.setStatus(Proposal.ProposalStatus.PENDING);
        return proposalRepository.save(proposal);
    }

    @Override
    public Proposal getProposalById(Long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposal not found"));
    }

    @Override
    public Proposal getProposalByProposalId(String proposalId) {
        return proposalRepository.findByProposalId(proposalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposal not found"));
    }

    @Override
    public Proposal updateProposal(Long id, Proposal updatedProposal) {
        Proposal existingProposal = getProposalById(id);
        
        if (existingProposal.getStatus() != Proposal.ProposalStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update non-pending proposal");
        }
        
        existingProposal.setCoverLetter(updatedProposal.getCoverLetter());
        existingProposal.setBidAmount(updatedProposal.getBidAmount());
        existingProposal.setDeliveryTime(updatedProposal.getDeliveryTime());
        
        return proposalRepository.save(existingProposal);
    }

    @Override
    public void deleteProposal(Long id) {
        Proposal proposal = getProposalById(id);
        proposalRepository.delete(proposal);
    }

    @Override
    public List<Proposal> getProposalsByJob(Job job) {
        return proposalRepository.findByJob(job);
    }

    @Override
    public List<Proposal> getProposalsByFreelancer(UserEntity freelancer) {
        return proposalRepository.findByFreelancer(freelancer);
    }

    @Override
    public Proposal updateProposalStatus(Long id, Proposal.ProposalStatus status) {
        Proposal proposal = getProposalById(id);
        proposal.setStatus(status);
        return proposalRepository.save(proposal);
    }

    @Override
    public boolean hasUserApplied(Job job, UserEntity freelancer) {
        return proposalRepository.findByJobAndFreelancer(job, freelancer).isPresent();
    }

    @Override
    public List<Proposal> getProposalsByStatus(Proposal.ProposalStatus status) {
        return proposalRepository.findByStatus(status);
    }
}
