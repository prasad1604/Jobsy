package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.Proposal;
import com.prasad.Jobsy.entity.UserEntity;

import java.util.List;

public interface ProposalService {
    Proposal createProposal(Proposal proposal);
    Proposal getProposalById(Long id);
    Proposal getProposalByProposalId(String proposalId);
    Proposal updateProposal(Long id, Proposal proposal);
    void deleteProposal(Long id);
    List<Proposal> getProposalsByJob(Job job);
    List<Proposal> getProposalsByFreelancer(UserEntity freelancer);
    Proposal updateProposalStatus(Long id, Proposal.ProposalStatus status);
    boolean hasUserApplied(Job job, UserEntity freelancer);
    List<Proposal> getProposalsByStatus(Proposal.ProposalStatus status);
}
