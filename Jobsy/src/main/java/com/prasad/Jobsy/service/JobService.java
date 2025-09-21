package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {
    Job createJob(Job job);
    Job getJobById(Long id);
    Job getJobByJobId(String jobId);
    Job updateJob(Long id, Job job);
    void deleteJob(Long id);
    Page<Job> getAllJobs(Pageable pageable);
    Page<Job> getJobsByStatus(Job.JobStatus status, Pageable pageable);
    Page<Job> searchJobs(String keyword, Pageable pageable);
    Page<Job> getJobsByCategory(String category, Pageable pageable);
    List<Job> getJobsByHirer(UserEntity hirer);
    Job updateJobStatus(Long id, Job.JobStatus status);
}
