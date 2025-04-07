package com.steerify.Controllers;

import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Dtos.Reusables.JobDto;
import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Entities.Talent;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.exception.UserNotFoundException;
import com.steerify.Helpers.config.SecurityConfig;
import com.steerify.Helpers.config.SecurityUtils;
import com.steerify.Mappers.Reusables.JobMapper;
import com.steerify.Repositories.Reusables.ApplicationRepository;
import com.steerify.Repositories.Reusables.JobRepository;
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
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

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


    @GetMapping("/jobs")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<JobDto>> getAllAvailableJobs() {
        return new ResponseEntity<>(jobService.getAllJobs()
                .stream().map(JobMapper::mapJobToDto)
                .toList(), HttpStatus.OK);
    }

//    update postman
    @PostMapping("/{talentId}/apply/{jobId}")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<ApplicationDto> createApplication(
            @PathVariable("talentId") UUID talentId,
            @PathVariable("jobId") UUID jobId,
            @RequestBody ApplicationDto applicationDto) {
        Talent talent = talentRepository.findById(talentId).orElseThrow(()-> new UserNotFoundException("User Not found"));
        Job job =jobRepository.findById(jobId).orElseThrow(()-> new ResourceNotFoundException("Job not found"));


        ApplicationDto application = applicationService.createApplication(applicationDto);
        application.setJobId(job.getJobId());
        application.setTalentId(talent.getTalentId());
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{talentId}/applications")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<ApplicationDto>> getTalentApplications(
            @PathVariable("talentId") UUID talentId
    ){
        return ResponseEntity.ok(applicationService.getApplicationsByTalentId(talentId));
    }



    @DeleteMapping("/{talentId}/{applicationId}")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<String> deleteApplication(
            @PathVariable("talentId") UUID talentId,
            @PathVariable("applicationId") UUID applicationId) {

        return new ResponseEntity<>(applicationService.deleteApplication(talentId,applicationId),HttpStatus.OK);
    }

    @PostMapping("{talentId}/post")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<PostDto> createPost(@PathVariable("talentId")UUID talentId,@Valid @RequestBody PostDto postDto) {
        Talent foundTalent = talentRepository.findById(talentId).orElseThrow(()-> new ResourceNotFoundException("Talent not found"));

        PostDto createdPost = postService.createPost(postDto, foundTalent.getFirstName(),
                foundTalent.getLastName(),  foundTalent.getTalentId(), foundTalent.getPhoneNumber(),foundTalent.getRole());
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('CLIENT') or hasRole('TALENT')")
    public ResponseEntity<List<PostDto>> getTalentPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }



    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("postId") UUID postId, @Valid @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(postId, postDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") UUID postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/about-talent/{talentId}")
    public ResponseEntity<TalentDto> getTalentById(@PathVariable("talentId") UUID talentId) {
        return new ResponseEntity<>(talentServices.findById(talentId), HttpStatus.FOUND);
    }


    @DeleteMapping("/{talentId}")
    public ResponseEntity<Void> deleteTalent(@PathVariable("talentId") UUID talentId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}