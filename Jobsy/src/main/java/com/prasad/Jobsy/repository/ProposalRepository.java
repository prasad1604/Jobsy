package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.Proposal;
import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Optional<Proposal> findByProposalId(String proposalId);
    List<Proposal> findByJob(Job job);
    List<Proposal> findByFreelancer(UserEntity freelancer);
    Optional<Proposal> findByJobAndFreelancer(Job job, UserEntity freelancer);
    List<Proposal> findByStatus(Proposal.ProposalStatus status);
}
