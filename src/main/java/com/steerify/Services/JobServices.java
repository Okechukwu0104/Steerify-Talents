package com.steerify.Services;

import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Reusables.Job;

import java.util.List;
import java.util.UUID;

public interface JobServices {
     Job createJob(JobDto jobDto) ;
     List<Job> getAllJobs();
     Job getJobById(UUID jobId);
     List<Job> getJobsByClientId(UUID clientId);
     Job updateJob(UUID jobId, JobDto jobDto) ;
     void deleteJob(UUID jobId) ;

    }
