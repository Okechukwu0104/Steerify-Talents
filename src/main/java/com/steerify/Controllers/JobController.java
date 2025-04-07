//package com.steerify.Controllers;
//
//
//import com.steerify.Dtos.Reusables.ApplicationDto;
//import com.steerify.Dtos.Reusables.JobDto;
//import com.steerify.Entities.Reusables.Job;
//import com.steerify.Services.ApplicationService;
//import com.steerify.Services.JobServices;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/jobs")
//@RequiredArgsConstructor
//public class JobController {
//
//    private final JobServices jobService;
//    private final ApplicationService applicationService;
//
//    @PostMapping
//
//
//
//
//    @PutMapping("/{jobId}")
//    public ResponseEntity<Job> updateJob(@PathVariable UUID jobId, @RequestBody JobDto jobDto) {
//        Job job = jobService.updateJob(jobId, jobDto);
//        return ResponseEntity.ok(job);
//    }
//
//    @GetMapping("/job/{jobId}")
//    public ResponseEntity<List<ApplicationDto>> getApplicationsByJobId(@PathVariable UUID jobId) {
//        List<ApplicationDto> applications = applicationService.getApplicationsByJobId(jobId);
//        return ResponseEntity.ok(applications);
//    }
//

//}
