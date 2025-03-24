package com.steerify.Mappers.Reusables;

import com.steerify.Entities.Reusables.Job;
import com.steerify.Dtos.Reusables.JobDto;

import java.util.UUID;

public class JobMapper {

    public static JobDto mapJobToDto(Job job) {
        return new JobDto(
                job.getJobId(),
                job.getClientId(),
                job.getClientName(),
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills(),
                job.getLocation(),
                job.getPayment(),
                job.getDeadline()
        );
    }

    public static Job mapDtoToJob(JobDto jobDto) {
        return new Job(
                UUID.randomUUID(),
                jobDto.getClientId(),
                jobDto.getClientName(),
                jobDto.getTitle(),
                jobDto.getDescription(),
                jobDto.getRequiredSkills(),
                jobDto.getLocation(),
                jobDto.getPayment(),
                jobDto.getDeadline()
        );
    }
}