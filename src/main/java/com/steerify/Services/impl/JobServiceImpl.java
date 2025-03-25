package com.steerify.Services.impl;
import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Mappers.Reusables.JobMapper;
import com.steerify.Repositories.Reusables.JobRepository;
import com.steerify.Services.JobServices;
import com.steerify.exceptions.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobServices {

    private final JobRepository jobRepository;

    public Job createJob(JobDto jobDto) {
        return jobRepository.save(JobMapper.mapDtoToJob(jobDto));
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(UUID jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found"));
    }

    public List<Job> getJobsByClientId(UUID clientId) {
        return jobRepository.findByClientId(clientId);
    }

    public Job updateJob(UUID jobId, JobDto jobDto) {
        Job job = getJobById(jobId);
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setRequiredSkills(jobDto.getRequiredSkills());
        job.setLocation(jobDto.getLocation());
        job.setPayment(jobDto.getPayment());
        job.setDeadline(jobDto.getDeadline());
        return jobRepository.save(job);
    }

    public void deleteJob(UUID jobId) {
        jobRepository.deleteById(jobId);
    }
}
