package com.steerify.Repositories;

import com.steerify.Entities.Reusables.Job;
import com.steerify.Repositories.Reusables.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;


    @Test
    void testSaveJob() {
        Job job = new Job();
        job.setJobId(UUID.randomUUID());
        job.setTitle("Software Engineer");
        job.setLocation("Lagos");
        job.setDescription("Someone that is proficient in React and the likes");
        job.setClientName("Steerify Group");
        job.setClientId(UUID.randomUUID());
        job.setDeadline("MArch 29th");
        job.setPayment("500k per annum");

        Job savedJob = jobRepository.save(job);

        assertNotNull(savedJob);
        assertEquals(job.getTitle(), savedJob.getTitle());
    }

    @Test
    void testFindJobById() {
        Job job = new Job();
        job.setJobId(UUID.randomUUID());
        job.setTitle("Software Engineer");
        jobRepository.save(job);

        Job foundJob = jobRepository.findById(job.getJobId()).orElse(null);

        assertNotNull(foundJob);
        assertEquals(job.getJobId(), foundJob.getJobId());
    }

    @Test
    void testFindJobsByClientId() {
        long initial = jobRepository.count();
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
        long finalCount = jobRepository.count();

        List<Job> jobs = jobRepository.findByClientId(clientId);

        assertEquals(2, (finalCount - initial));
    }

    @Test
    void testDeleteJob() {
        Job job = new Job();
        job.setJobId(UUID.randomUUID());
        job.setTitle("Software Engineer");
        jobRepository.save(job);

        jobRepository.deleteById(job.getJobId());

        assertFalse(jobRepository.findById(job.getJobId()).isPresent());
    }
}