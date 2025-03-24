package com.steerify.Services;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    Application createApplication(ApplicationDto applicationDto);
    Application getApplicationById(UUID applicationId);
    List<Application> getApplicationsByTalentId(UUID talentId);
    List<Application> getApplicationsByJobId(UUID jobId) ;
    List<Application> getApplicationsByCoverLetterContent(String searchTerm) ;
    Application updateApplicationStatus(UUID applicationId, ApplicationStatus status);
    void deleteApplication(UUID applicationId);

    }
