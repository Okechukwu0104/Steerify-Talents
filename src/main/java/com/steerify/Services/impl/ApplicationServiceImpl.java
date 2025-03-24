package com.steerify.Services.impl;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Repositories.Reusables.ApplicationRepository;
import com.steerify.Services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public Application createApplication(ApplicationDto applicationDto) {
        Application application = new Application();
        application.setApplicationId(UUID.randomUUID());
        application.setTalentId(applicationDto.getTalentId());
        application.setJobId(applicationDto.getJobId());
        application.setCoverLetter(applicationDto.getCoverLetter());
        application.setStats(ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }

    public Application getApplicationById(UUID applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public List<Application> getApplicationsByTalentId(UUID talentId) {
        return applicationRepository.findByTalentId(talentId);
    }

    public List<Application> getApplicationsByJobId(UUID jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> getApplicationsByCoverLetterContent(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return applicationRepository.findByCoverLetterContaining(searchTerm);
    }

    public Application updateApplicationStatus(UUID applicationId, ApplicationStatus status) {
        Application application = getApplicationById(applicationId);
        application.setStats(status);
        return applicationRepository.save(application);
    }

    public void deleteApplication(UUID applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}
