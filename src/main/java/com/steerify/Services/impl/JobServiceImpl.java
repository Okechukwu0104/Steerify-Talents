package com.steerify.Services.impl;
import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Mappers.Reusables.JobMapper;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.Reusables.JobRepository;
import com.steerify.Services.JobServices;
import com.steerify.exceptions.JobNotFoundException;
import com.steerify.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobServices {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;


    public Job createJob(JobDto jobDto) {
        if (jobDto.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }

        if (jobDto.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }
        Client client = clientRepository.findById(jobDto.getClientId())
                .orElseThrow(()-> new JobNotFoundException("Client not found"));
        return jobRepository.save(JobMapper.mapDtoToJob(jobDto));

    }

    public List<Job> getSortedJobs() {
        List<Job> jobs = jobRepository.findAll();

        jobs.sort(Comparator.comparing(
                Job::getDeadline,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));

        return jobs;
    }


    public List<Job> getAllJobs() {
            LocalDate today = java.time.LocalDate.now();
            return jobRepository.findAll().stream()
                    .filter(job -> !job.getDeadline().isBefore(today))
                    .collect(Collectors.toList());

    }

    public Job getJobById(UUID jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found"));
    }

    public List<Job> getJobsByClientId(UUID clientId) {
        return jobRepository.findByClientId(clientId);
    }

    public Job updateJob(UUID jobId, JobDto patchDto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (patchDto.getTitle() != null) {
            job.setTitle(patchDto.getTitle());
        }

        if (patchDto.getDescription() != null) {
            job.setDescription(patchDto.getDescription());
        }

        if (patchDto.getRequiredSkills() != null) {
            job.setRequiredSkills(patchDto.getRequiredSkills());
        }

        if (patchDto.getLocation() != null) {
            job.setLocation(patchDto.getLocation());
        }

        if (patchDto.getPayment() != null) {
            job.setPayment(patchDto.getPayment());
        }

        if (patchDto.getDeadline() != null) {
            LocalDate deadline = patchDto.getDeadline();
            if (deadline.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Deadline cannot be in the past");
            }
            job.setDeadline(patchDto.getDeadline());
        }

        return jobRepository.save(job);
    }

    public void deleteJob(UUID jobId) {
        jobRepository.deleteById(jobId);
    }
}
