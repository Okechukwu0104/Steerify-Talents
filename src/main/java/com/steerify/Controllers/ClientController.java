package com.steerify.Controllers;

import com.steerify.Dtos.ClientDto;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Enums.ApplicationStatus;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Services.*;
import com.steerify.Services.impl.TalentServicesImpl;
import com.steerify.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private final PostServices postServices;
    private final ClientRepository clientRepository;
    private final TalentServices talentServices;


    @PostMapping("/signup")
        @PreAuthorize("permitAll()")
        public ResponseEntity<ClientDto> signUp(@Valid @RequestBody ClientDto clientDto) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clientService.createClient(clientDto));
        }

        @PostMapping("/login")
        @PreAuthorize("permitAll()")
        public ResponseEntity<LoginResponseDto> loginClient(@Valid @RequestBody LoginRequestDto loginDto) {
            return ResponseEntity.ok(clientService.login(loginDto));
        }



        @GetMapping
        @PreAuthorize("permitAll()")
        public ResponseEntity<List<ClientDto>> getAllClients() {
            return ResponseEntity.ok(clientService.getAllClients());
        }

        @PutMapping("/update/{clientId}")
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

    @GetMapping("/search")
    public ResponseEntity<List<ApplicationDto>> searchApplicationsByCoverLetter(
            @RequestParam String keyword) {
        List<ApplicationDto> applications = applicationService.getApplicationsByCoverLetterContent(keyword);
        return ResponseEntity.ok(applications);
    }

//    update postman

        @PostMapping("/{clientId}/jobs/create")
        @PreAuthorize("hasRole('CLIENT')")
        public ResponseEntity<Job> createJob(@PathVariable("clientId") UUID clientId ,@RequestBody JobDto jobDto) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(jobService.createJob(clientId,jobDto));
        }

        @GetMapping("/{clientId}/jobs")
        @PreAuthorize("hasRole('CLIENT')")
        public ResponseEntity<List<Job>> getClientJobs(@PathVariable UUID clientId) {
            return ResponseEntity.ok(jobService.getJobsByClientId(clientId));
        }



        @GetMapping("/jobs/{jobId}/applications")
        @PreAuthorize("hasRole('CLIENT')")
        public ResponseEntity<List<ApplicationDto>> getApplicationsForJob(
                @PathVariable UUID jobId) {
            return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
        }


        @PutMapping("/application/{applicationId}/status")
        @PreAuthorize("hasRole('CLIENT')")
        public ResponseEntity<ApplicationDto> updateApplicationStatus(
                @PathVariable("applicationId") UUID applicationId,
                @RequestParam ApplicationStatus status) {

            return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, status));
        }

    @PostMapping("{clientId}/post")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TALENT')")
    public ResponseEntity<PostDto> createPost(@PathVariable("clientId")UUID clientId,@Valid @RequestBody PostDto postDto) {
        Client foundClient = clientRepository.findById(clientId).orElseThrow(()-> new ResourceNotFoundException("Talent not found"));
        PostDto createdPost = postServices.createPost(postDto, foundClient.getFirstName(),
                foundClient.getLastName(),  foundClient.getClientId(), foundClient.getPhone(), foundClient.getRole());

        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TALENT')")
    public ResponseEntity<List<PostDto>> getTalentPosts() {
        return ResponseEntity.ok(postServices.getAllPosts());
    }

    @GetMapping("/{talentId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TALENT')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByTalentId(@PathVariable UUID talentId) {
        List<ApplicationDto> applications = applicationService.getApplicationsByTalentId(talentId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/application/{jobId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TALENT')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByJobId(@PathVariable("jobId") UUID jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    @PutMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Job> updateClientJob(
            @PathVariable UUID jobId,
            @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.updateJob(jobId, jobDto));
    }



    @DeleteMapping("/delete-job/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable UUID jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok("job deleted successfully");
    }


    @GetMapping("/about-client/{clientId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClientDto> getClientById(@PathVariable UUID clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

}

