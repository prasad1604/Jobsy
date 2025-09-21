package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public Job createJob(Job job) {
        job.setJobId(UUID.randomUUID().toString());
        job.setStatus(Job.JobStatus.OPEN);
        return jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @Override
    public Job getJobByJobId(String jobId) {
        return jobRepository.findByJobId(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @Override
    public Job updateJob(Long id, Job updatedJob) {
        Job existingJob = getJobById(id);
        
        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setCategory(updatedJob.getCategory());
        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setBudget(updatedJob.getBudget());
        existingJob.setDeadline(updatedJob.getDeadline());
        
        return jobRepository.save(existingJob);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }

    @Override
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Override
    public Page<Job> getJobsByStatus(Job.JobStatus status, Pageable pageable) {
        return jobRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable);
    }

    @Override
    public Page<Job> getJobsByCategory(String category, Pageable pageable) {
        return jobRepository.findByCategoryContainingIgnoreCase(category, pageable);
    }

    @Override
    public List<Job> getJobsByHirer(UserEntity hirer) {
        return jobRepository.findByHirer(hirer);
    }

    @Override
    public Job updateJobStatus(Long id, Job.JobStatus status) {
        Job job = getJobById(id);
        job.setStatus(status);
        return jobRepository.save(job);
    }
}
