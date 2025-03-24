package com.steerify.Controllers;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody ApplicationDto applicationDto) {
        Application application = applicationService.createApplication(applicationDto);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable UUID applicationId) {
        Application application = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/talent/{talentId}")
    public ResponseEntity<List<Application>> getApplicationsByTalentId(@PathVariable UUID talentId) {
        List<Application> applications = applicationService.getApplicationsByTalentId(talentId);
        return ResponseEntity.ok(applications);
    }



    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable UUID applicationId,
            @RequestParam ApplicationStatus status) {
        Application application = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteApplication(@PathVariable UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.ok("deleted successfully");
    }
    @GetMapping("/search")
    public ResponseEntity<List<Application>> searchApplicationsByCoverLetter(
            @RequestParam String keyword) {
        List<Application> applications = applicationService.getApplicationsByCoverLetterContent(keyword);
        return ResponseEntity.ok(applications);
    }
}
