package com.steerify.Controllers;


import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Services.ApplicationService;
import com.steerify.Services.JobServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobServices jobService;
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobDto jobDto) {
        Job job = jobService.createJob(jobDto);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID jobId) {
        Job job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Job>> getJobsByClientId(@PathVariable UUID clientId) {
        List<Job> jobs = jobService.getJobsByClientId(clientId);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable UUID jobId, @RequestBody JobDto jobDto) {
        Job job = jobService.updateJob(jobId, jobDto);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId(@PathVariable UUID jobId) {
        List<Application> applications = applicationService.getApplicationsByJobId(jobId);
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
