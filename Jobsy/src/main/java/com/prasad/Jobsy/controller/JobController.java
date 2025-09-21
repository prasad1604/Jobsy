package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Job;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Job createJob(
            @Valid @RequestBody Job job,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity hirer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        job.setHirer(hirer);
        return jobService.createJob(job);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/job-id/{jobId}")
    public ResponseEntity<Job> getJobByJobId(@PathVariable String jobId) {
        Job job = jobService.getJobByJobId(jobId);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody Job job,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Job existingJob = jobService.getJobById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!existingJob.getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own jobs");
        }
        
        Job updatedJob = jobService.updateJob(id, job);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Job job = jobService.getJobById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!job.getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own jobs");
        }
        
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sortObj = direction.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        Page<Job> jobs = jobService.getAllJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Job>> getJobsByStatus(
            @PathVariable Job.JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getJobsByStatus(status, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Job>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.searchJobs(keyword, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Job>> getJobsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getJobsByCategory(category, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<Job>> getMyJobs(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity hirer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Job> jobs = jobService.getJobsByHirer(hirer);
        return ResponseEntity.ok(jobs);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Job> updateJobStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Job job = jobService.getJobById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!job.getHirer().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own jobs");
        }
        
        Job.JobStatus status = Job.JobStatus.valueOf(request.get("status"));
        Job updatedJob = jobService.updateJobStatus(id, status);
        return ResponseEntity.ok(updatedJob);
    }
}
