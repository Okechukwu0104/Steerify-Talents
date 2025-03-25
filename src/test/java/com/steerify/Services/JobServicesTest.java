package com.steerify.Services;

import static org.junit.jupiter.api.Assertions.*;

import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Repositories.Reusables.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest
class JobServicesTest {

    @Autowired
    private JobServices jobServices;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void testCreateJob() {
        JobDto jobDto = new JobDto();
        jobDto.setTitle("Software Engineer");
        jobDto.setLocation("Lagos");
        jobDto.setDescription("Proficient in React");
        jobDto.setClientName("Steerify Group");
        jobDto.setClientId(UUID.randomUUID());
        jobDto.setDeadline("March 29th");
        jobDto.setPayment("500k per annum");
        jobDto.setRequiredSkills(Arrays.asList("Java", "Spring"));

        Job createdJob = jobServices.createJob(jobDto);

        assertNotNull(createdJob);
        assertEquals(jobDto.getTitle(), createdJob.getTitle());
        assertNotNull(jobRepository.findById(createdJob.getJobId()).orElse(null));
    }

    @Test
    void testGetAllJobs() {
        Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setTitle("Software Engineer");
        jobRepository.save(job1);

        Job job2 = new Job();
        job2.setJobId(UUID.randomUUID());
        job2.setTitle("Data Scientist");
        jobRepository.save(job2);

        List<Job> jobs = jobServices.getAllJobs();

        assertTrue(jobs.stream().anyMatch(j -> j.getTitle().equals("Software Engineer")));
        assertTrue(jobs.stream().anyMatch(j -> j.getTitle().equals("Data Scientist")));
    }

    @Test
    void testGetJobById() {
        Job job = new Job();
        job.setJobId(UUID.randomUUID());
        job.setTitle("Software Engineer");
        jobRepository.save(job);

        Job foundJob = jobServices.getJobById(job.getJobId());

        assertNotNull(foundJob);
        assertEquals(job.getJobId(), foundJob.getJobId());
    }

    @Test
    void testGetJobsByClientId() {
        UUID clientId = UUID.randomUUID();

        Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setClientId(clientId);
        job1.setTitle("Software Engineer");

        Job job2 = new Job();
        job2.setJobId(UUID.randomUUID());
        job2.setClientId(clientId);
        job2.setTitle("Data Scientist");

        jobRepository.saveAll(Arrays.asList(job1, job2));

        List<Job> jobs = jobServices.getJobsByClientId(clientId);

        assertEquals(2, jobs.size());
        assertTrue(jobs.stream().allMatch(j -> j.getClientId().equals(clientId)));
    }

    @Test
    void testUpdateJob() {
        Job existingJob = new Job();
        existingJob.setJobId(UUID.randomUUID());
        existingJob.setTitle("I will make your full website");
        existingJob.setDescription("Using HTML,css,js");
        existingJob.setPayment("5000 naira");
        existingJob.setDeadline("march 2024");
        jobRepository.save(existingJob);

        JobDto updateDto = new JobDto();
        updateDto.setTitle("New Title");
        updateDto.setDescription("New Description");
        existingJob.setDescription("Using React,Tailwind css");
        existingJob.setPayment("5M naira");
        existingJob.setDeadline("march 2026");

        Job updatedJob = jobServices.updateJob(existingJob.getJobId(), updateDto);

        assertEquals(existingJob.getJobId(), updatedJob.getJobId());
        assertEquals("New Title", updatedJob.getTitle());
        assertEquals("New Description", updatedJob.getDescription());
    }

    @Test
    void testDeleteJob_service() {
        Job job = new Job();
        job.setJobId(UUID.randomUUID());
        job.setTitle("To be deleted");
        jobRepository.save(job);

        jobServices.deleteJob(job.getJobId());

        assertFalse(jobRepository.findById(job.getJobId()).isPresent());
    }


}