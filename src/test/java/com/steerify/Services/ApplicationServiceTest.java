package com.steerify.Services;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Repositories.Reusables.ApplicationRepository;
import com.steerify.Services.impl.ApplicationServiceImpl;
import com.steerify.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(ApplicationServiceImpl.class)
class ApplicationServiceTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationServiceImpl applicationService;

    private Application testApplication;

    @BeforeEach
    void setUp() {
        testApplication = new Application();
        testApplication.setApplicationId(UUID.randomUUID());
        testApplication.setTalentId(UUID.randomUUID());
        testApplication.setJobId(UUID.randomUUID());
        testApplication.setCoverLetter("Experienced developer looking for new opportunities");
        testApplication.setStats(ApplicationStatus.PENDING);
        applicationRepository.save(testApplication);
    }

    @AfterEach
    void tearDown() {
        applicationRepository.deleteAll();
    }

    @Test
    void createApplication_ShouldSaveAndReturnApplicationDto() {
        ApplicationDto dto = new ApplicationDto();
        dto.setTalentId(UUID.randomUUID());
        dto.setJobId(UUID.randomUUID());
        dto.setCoverLetter("New application cover letter");

        ApplicationDto result = applicationService.createApplication(dto);

        assertNotNull(result.getApplicationId());
        assertEquals(dto.getCoverLetter(), result.getCoverLetter());
        assertEquals(ApplicationStatus.PENDING, result.getStats());
        assertTrue(applicationRepository.existsById(result.getApplicationId()));
    }

    @Test
    void getApplicationById_ShouldReturnApplication_WhenExists() {
        ApplicationDto result = applicationService.getApplicationById(testApplication.getApplicationId());

        assertNotNull(result);
        assertEquals(testApplication.getApplicationId(), result.getApplicationId());
        assertEquals(testApplication.getCoverLetter(), result.getCoverLetter());
    }

    @Test
    void getApplicationById_ShouldThrowException_WhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.getApplicationById(nonExistentId));
    }

    @Test
    void getApplicationsByTalentId_ShouldReturnMatchingApplications() {
        UUID talentId = UUID.randomUUID();
        Application app1 = createTestApplication(talentId, UUID.randomUUID());
        Application app2 = createTestApplication(talentId, UUID.randomUUID());
        createTestApplication(UUID.randomUUID(), UUID.randomUUID());

        List<ApplicationDto> results = applicationService.getApplicationsByTalentId(talentId);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(dto -> dto.getTalentId().equals(talentId)));
    }

    @Test
    void getApplicationsByJobId_ShouldReturnMatchingApplications() {
        UUID jobId = UUID.randomUUID();
        Application app1 = createTestApplication(UUID.randomUUID(), jobId);
        Application app2 = createTestApplication(UUID.randomUUID(), jobId);
        createTestApplication(UUID.randomUUID(), UUID.randomUUID());

        List<ApplicationDto> results = applicationService.getApplicationsByJobId(jobId);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(dto -> dto.getJobId().equals(jobId)));
    }

    @Test
    void getApplicationsByCoverLetterContent_ShouldReturnMatchingApplications() {
        String searchTerm = "experienced";
        Application app1 = createTestApplicationWithCoverLetter("I'm an experienced Python developer");
        Application app2 = createTestApplicationWithCoverLetter("Experienced in Spring Boot");
        createTestApplicationWithCoverLetter("Looking for second opportunity");

        List<ApplicationDto> results = applicationService.getApplicationsByCoverLetterContent(searchTerm);

        assertTrue(results.stream()
                .allMatch(dto -> dto.getCoverLetter().toLowerCase().contains(searchTerm)));
    }

    @Test
    void getApplicationsByCoverLetterContent_ShouldThrowException_WhenSearchTermEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> applicationService.getApplicationsByCoverLetterContent(""));
    }

    @Test
    void updateApplicationStatus_ShouldUpdateAndReturnApplication() {
        ApplicationStatus newStatus = ApplicationStatus.ACCEPTED;

        ApplicationDto result = applicationService.updateApplicationStatus(
                testApplication.getApplicationId(), newStatus);

        assertEquals(newStatus, result.getStats());
        assertEquals(newStatus,
                applicationRepository.findById(testApplication.getApplicationId()).get().getStats());
    }
//
//    @Test
//    void deleteApplication_ShouldRemoveApplication() {
//        applicationService.deleteApplication(testApplication.getApplicationId(), applicationId);
//
//        assertFalse(applicationRepository.existsById(testApplication.getApplicationId()));
//    }

//    @Test
//    void deleteApplication_ShouldThrowException_WhenNotFound() {
//        UUID nonExistentId = UUID.randomUUID();
//        assertThrows(ResourceNotFoundException.class,
//                () -> applicationService.deleteApplication(nonExistentId, applicationId));
//    }

    private Application createTestApplication(UUID talentId, UUID jobId) {
        Application app = new Application();
        app.setApplicationId(UUID.randomUUID());
        app.setTalentId(talentId);
        app.setJobId(jobId);
        app.setCoverLetter("Test application");
        app.setStats(ApplicationStatus.PENDING);
        return applicationRepository.save(app);
    }

    private Application createTestApplicationWithCoverLetter(String coverLetter) {
        Application app = new Application();
        app.setApplicationId(UUID.randomUUID());
        app.setTalentId(UUID.randomUUID());
        app.setJobId(UUID.randomUUID());
        app.setCoverLetter(coverLetter);
        app.setStats(ApplicationStatus.PENDING);
        return applicationRepository.save(app);
    }
}