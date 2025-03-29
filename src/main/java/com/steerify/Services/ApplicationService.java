package com.steerify.Services;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Enums.ApplicationStatus;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    ApplicationDto createApplication(ApplicationDto applicationDto);
    ApplicationDto getApplicationById(UUID applicationId);
    List<ApplicationDto> getApplicationsByTalentId(UUID talentId);
    List<ApplicationDto> getApplicationsByJobId(UUID jobId) ;
    List<ApplicationDto> getApplicationsByCoverLetterContent(String searchTerm) ;
    ApplicationDto updateApplicationStatus(UUID applicationId, ApplicationStatus status);
    String deleteApplication(UUID talentId, UUID applicationId);

    List<ApplicationDto> getAllApplications();
}
