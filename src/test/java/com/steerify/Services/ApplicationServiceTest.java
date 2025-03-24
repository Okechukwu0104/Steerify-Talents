package com.steerify.Services;

import static org.junit.jupiter.api.Assertions.*;


import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Repositories.Reusables.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.UUID;


@SpringBootTest
@DataMongoTest
class ApplicationServiceTest {
    private ApplicationService applicationService;

    private ApplicationRepository applicationRepository;



    @Test
    void testCreateApplication() {
        ApplicationDto dto = new ApplicationDto(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                "I'm excited to apply for this position",
                null
        );

        Application created = applicationService.createApplication(dto);

        assertNotNull(created.getApplicationId());
        assertEquals(dto.getTalentId(), created.getTalentId());
        assertEquals(dto.getJobId(), created.getJobId());
        assertEquals(dto.getCoverLetter(), created.getCoverLetter());
        assertEquals(ApplicationStatus.PENDING, created.getStats());
        assertTrue(applicationRepository.findById(created.getApplicationId()).isPresent());
    }

    @Test
    void testGetApplicationById() {
        Application savedApp = applicationRepository.save(new Application(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test cover letter",
                ApplicationStatus.PENDING
        ));

        Application found = applicationService.getApplicationById(savedApp.getApplicationId());

        assertEquals(savedApp.getApplicationId(), found.getApplicationId());
        assertEquals(savedApp.getCoverLetter(), found.getCoverLetter());
    }

    @Test
    void testGetApplicationById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            applicationService.getApplicationById(nonExistentId);
        });
    }

    @Test
    void testGetApplicationsByTalentId() {
        UUID talentId = UUID.randomUUID();
        applicationRepository.saveAll(List.of(
                new Application(UUID.randomUUID(), talentId, UUID.randomUUID(), "App 1", ApplicationStatus.PENDING),
                new Application(UUID.randomUUID(), talentId, UUID.randomUUID(), "App 2", ApplicationStatus.REVIEWED),
                new Application(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Other talent", ApplicationStatus.PENDING)
        ));

        List<Application> applications = applicationService.getApplicationsByTalentId(talentId);

        assertEquals(2, applications.size());
        assertTrue(applications.stream().allMatch(app -> app.getTalentId().equals(talentId)));
    }

    @Test
    void testGetApplicationsByJobId() {
        UUID jobId = UUID.randomUUID();
        applicationRepository.saveAll(List.of(
                new Application(UUID.randomUUID(), UUID.randomUUID(), jobId, "App 1", ApplicationStatus.PENDING),
                new Application(UUID.randomUUID(), UUID.randomUUID(), jobId, "App 2", ApplicationStatus.ACCEPTED),
                new Application(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Other job", ApplicationStatus.PENDING)
        ));

        List<Application> applications = applicationService.getApplicationsByJobId(jobId);

        assertEquals(2, applications.size());
        assertTrue(applications.stream().allMatch(app -> app.getJobId().equals(jobId)));
    }

    @Test
    void testGetApplicationsByCoverLetterContent() {
        applicationRepository.saveAll(List.of(
                new Application(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        "I'm passionate about software development", ApplicationStatus.PENDING),
                new Application(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        "Passionate developer with 5 years experience", ApplicationStatus.REVIEWED),
                new Application(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        "Looking for new opportunities", ApplicationStatus.PENDING)
        ));

        List<Application> results = applicationService.getApplicationsByCoverLetterContent("passionate");

        assertEquals(2, results.size());
        assertTrue(results.stream()
                .allMatch(app -> app.getCoverLetter().toLowerCase().contains("passionate")));
    }

    @Test
    void testGetApplicationsByCoverLetterContent_EmptySearch() {
        assertThrows(IllegalArgumentException.class, () -> {
            applicationService.getApplicationsByCoverLetterContent("");
        });
    }

    @Test
    void testUpdateApplicationStatus() {
        Application app = applicationRepository.save(new Application(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test application",
                ApplicationStatus.PENDING
        ));

        Application updated = applicationService.updateApplicationStatus(
                app.getApplicationId(),
                ApplicationStatus.ACCEPTED
        );

        assertEquals(ApplicationStatus.ACCEPTED, updated.getStats());
        assertEquals(app.getApplicationId(), updated.getApplicationId());

        Application dbApp = applicationRepository.findById(app.getApplicationId()).get();
        assertEquals(ApplicationStatus.ACCEPTED, dbApp.getStats());
    }

    @Test
    void testUpdateApplicationStatus_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            applicationService.updateApplicationStatus(nonExistentId, ApplicationStatus.ACCEPTED);
        });
    }

    @Test
    void testDeleteApplication() {
        Application app = applicationRepository.save(new Application(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "To be deleted now now",
                ApplicationStatus.PENDING
        ));

        applicationService.deleteApplication(app.getApplicationId());

        assertFalse(applicationRepository.existsById(app.getApplicationId()));
    }
}