package com.steerify.Controllers;

import com.steerify.Dtos.ClientDto;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Services.ApplicationService;
import com.steerify.Services.ClientService;
import com.steerify.Services.JobServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AuthService authService;
    private final JobServices jobService;
    private final ApplicationService applicationService;


    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClientDto> signUp(@Valid @RequestBody ClientDto clientDto){
        ClientDto createdClient = clientService.createClient(clientDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginResponseDto> loginClient(@Valid @RequestBody LoginRequestDto loginDto) {
        LoginResponseDto response = clientService.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClientDto> getClientById(@PathVariable UUID clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PutMapping("/{clientId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable UUID clientId,
            @Valid @RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(clientService.updateClient(clientId, clientDto));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/jobs/create")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Job> createJob(@RequestBody JobDto jobDto) {
        Job job = jobService.createJob(jobDto);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/{clientId}/jobs")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Job>> getClientJobs(@PathVariable("clientId") UUID clientId) {
        return ResponseEntity.ok(jobService.getJobsByClientId(clientId));
    }

    @PutMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Job> updateClientJob(
            @PathVariable("jobId") UUID jobId,
            @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.updateJob(jobId, jobDto));
    }

    @GetMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsForJob(
            @PathVariable("jobId") UUID jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable UUID applicationId,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, status));
    }
}
