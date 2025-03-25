package com.steerify.Controllers;

import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.config.SecurityConfig;
import com.steerify.Helpers.config.SecurityUtils;
import com.steerify.Repositories.Reusables.PostRepository;
import com.steerify.Repositories.TalentRepository;
import com.steerify.Services.ApplicationService;
import com.steerify.Services.JobServices;
import com.steerify.Services.PostServices;
import com.steerify.Services.TalentServices;
import com.steerify.Services.impl.PostServicesImpl;
import com.steerify.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.steerify.Controllers.PostController.getCurrentUserName;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/talents")
public class TalentsController {
    private final TalentServices talentServices;
    private final JobServices jobService;
    private final ApplicationService applicationService;
    private final PostServices postService;
    private final SecurityUtils securityUtils;
    private final TalentRepository talentRepository;

    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    ResponseEntity<TalentDto> registerResident(@Valid @RequestBody TalentDto talentDto) {
        TalentDto registeredResident = talentServices.signUp(talentDto);
        return new ResponseEntity<>(registeredResident, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginResponseDto> loginResident(@Valid @RequestBody LoginRequestDto loginDto) {
        LoginResponseDto response = talentServices.signIn(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<ClientDto>> searchForClientsCompanyName(@RequestParam String nameOfCompany) {
        List<ClientDto> foundClient = talentServices.findByCompanyName(nameOfCompany);
        return new ResponseEntity<>(foundClient, HttpStatus.OK);
    }


    @PostMapping("/applications")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestBody ApplicationDto applicationDto) {
        return ResponseEntity.ok(applicationService.createApplication(applicationDto));
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<ApplicationDto>> getTalentApplications() {
        UUID talentId = getCurrentTalentId();
        return ResponseEntity.ok(applicationService.getApplicationsByTalentId(talentId));
    }


    private UUID getCurrentTalentId() {
        if (!securityUtils.getCurrentUserRole().equals("ROLE_TALENT")) {
            throw new AccessDeniedException("Access denied");
        }
        String email = securityUtils.getCurrentUserEmail();
        return talentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Talent not found"))
                .getTalentId();
    }

    @DeleteMapping("/applications/{applicationId}")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }

    // Post Management
    @PostMapping("/posts")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        postDto.setName(getCurrentUserName());
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<PostDto>> getTalentPosts() {
        String username = getCurrentUserName();
        return ResponseEntity.ok(postService.getAllPosts());
    }

}