package com.steerify.Services.impl;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.exceptions.ResourceNotFoundException;
import com.steerify.Mappers.Reusables.ApplicationMapper;
import com.steerify.Repositories.Reusables.ApplicationRepository;
import com.steerify.Services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationDto createApplication(ApplicationDto applicationDto) {
        applicationDto.setApplicationId(UUID.randomUUID());
        applicationDto.setStats(ApplicationStatus.PENDING);
        Application savedApplication = applicationRepository.save(ApplicationMapper.mapToAppl(applicationDto));
        return ApplicationMapper.mapToApplDto(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDto getApplicationById(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        return ApplicationMapper.mapToApplDto(application);
    }

    @Override
    public List<ApplicationDto> getApplicationsByTalentId(UUID talentId) {
        return applicationRepository.findByTalentId(talentId).stream()
                .map(ApplicationMapper::mapToApplDto)
                .toList();
    }

    @Override
    public List<ApplicationDto> getApplicationsByJobId(UUID jobId) {
        return applicationRepository.findByJobId(jobId).stream()
                .map(ApplicationMapper::mapToApplDto)
                .toList();
    }

    @Override
    public List<ApplicationDto> getApplicationsByCoverLetterContent(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }
        return applicationRepository.findByCoverLetterContaining(searchTerm).stream()
                .map(ApplicationMapper::mapToApplDto)
                .toList();
    }

    @Override
    public ApplicationDto updateApplicationStatus(UUID applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        application.setStats(status);
        Application updatedApplication = applicationRepository.save(application);
        return ApplicationMapper.mapToApplDto(updatedApplication);
    }

    @Override
    public void deleteApplication(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        applicationRepository.deleteById(applicationId);


    }

    @Override
    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll()
                .stream().map(ApplicationMapper::mapToApplDto)
                .toList();
    }
}

