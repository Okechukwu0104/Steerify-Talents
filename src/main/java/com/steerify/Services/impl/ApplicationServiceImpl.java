package com.steerify.Services.impl;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Helpers.auth.exception.UserNotFoundException;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.Reusables.JobRepository;
import com.steerify.Repositories.TalentRepository;
import com.steerify.exceptions.DuplicateEntityException;
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
    private final JobRepository jobRepository;
    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;

    @Override
    public ApplicationDto createApplication(ApplicationDto applicationDto) {
        applicationDto.setStats(ApplicationStatus.PENDING);

        Job job = jobRepository.findById(applicationDto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (applicationRepository.existsByTalentIdAndJobId(applicationDto.getTalentId(), applicationDto.getJobId())) {
            throw new DuplicateEntityException("You have already applied to this job");
        }
        Application savedApplication = applicationRepository.save(ApplicationMapper.mapToAppl(applicationDto));
        return ApplicationMapper.mapToApplDto(savedApplication);
    }

    @Override
//    @Transactional(readOnly = true)
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
    public String deleteApplication(UUID talentId, UUID applicationId) {
        talentRepository.findById(talentId).orElseThrow(() -> new UserNotFoundException("Talent not found with id: " + talentId));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        if (!application.getTalentId().equals(talentId)) {
            throw new IllegalArgumentException("You can only delete your applications");
        }
        applicationRepository.deleteById(applicationId);
        UUID jobId = application.getJobId();
        Job job =jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        Client client = clientRepository.findById(job.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + job.getClientId()));

        return "Application to "+client.getCompanyName()+"'s "+job.getTitle()+" deleted successfully";
    }

    @Override
    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll()
                .stream().map(ApplicationMapper::mapToApplDto)
                .toList();
    }
}

