package com.steerify.Controllers;

import com.steerify.Dtos.AdminDto;
import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Dtos.Reusables.PostDto;
import com.steerify.Entities.Reusables.Job;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Services.AdminService;
import com.steerify.Services.ApplicationService;
import com.steerify.Services.JobServices;
import com.steerify.Services.impl.PostServicesImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final JobServices jobService;
    private final PostServicesImpl postService;
    private final ApplicationService applicationService;



    @PostMapping("/signUp")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        AdminDto createdAdmin = adminService.createAdmin(adminDto);
        return ResponseEntity.ok(createdAdmin);
    }

    @PostMapping("/login")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoginResponseDto> loginClient(@Valid @RequestBody LoginRequestDto loginDto) {
        LoginResponseDto response = adminService.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{adminId}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable UUID adminId) {
        AdminDto adminDto = adminService.getAdminById(adminId);
        return ResponseEntity.ok(adminDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        List<AdminDto> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDto> updateAdmin(
            @PathVariable UUID adminId,
            @RequestBody AdminDto adminDto) {
        AdminDto updatedAdmin = adminService.updateAdmin(adminId, adminDto);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Job>> getAllJobsForAdmin() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @DeleteMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminDeleteJob(@PathVariable UUID jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostDto>> getAllPostsForAdmin() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminDeletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDto>> getAllApplicationsForAdmin() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }


}
