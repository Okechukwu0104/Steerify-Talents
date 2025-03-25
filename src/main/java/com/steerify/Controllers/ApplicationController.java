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
    public ResponseEntity<ApplicationDto> createApplication(@RequestBody ApplicationDto applicationDto) {
        ApplicationDto application = applicationService.createApplication(applicationDto);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable UUID applicationId) {
        ApplicationDto application = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/talent/{talentId}")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByTalentId(@PathVariable UUID talentId) {
        List<ApplicationDto> applications = applicationService.getApplicationsByTalentId(talentId);
        return ResponseEntity.ok(applications);
    }


    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable UUID applicationId,
            @RequestParam ApplicationStatus status) {
        ApplicationDto application = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteApplication(@PathVariable UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.ok("deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ApplicationDto>> searchApplicationsByCoverLetter(
            @RequestParam String keyword) {
        List<ApplicationDto> applications = applicationService.getApplicationsByCoverLetterContent(keyword);
        return ResponseEntity.ok(applications);
    }
}
