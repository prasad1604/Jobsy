package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByJobId(String jobId);
    List<Job> findByHirer(UserEntity hirer);
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
    Page<Job> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
    Page<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title, String description, Pageable pageable);
}
