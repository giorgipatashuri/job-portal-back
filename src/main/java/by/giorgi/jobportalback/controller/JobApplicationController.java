package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.JobApplicationDto;
import by.giorgi.jobportalback.model.dto.request.JobApplicationReq;
import by.giorgi.jobportalback.model.entity.Company;
import by.giorgi.jobportalback.model.entity.JobApplication;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.ApplicationStatus;
import by.giorgi.jobportalback.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<JobApplication> applyForJob(@RequestBody JobApplicationReq request , @AuthenticationPrincipal User user) {
        JobApplication application = jobApplicationService.applyForJob(
                request.getJobId(),
                request.getCvId(),
                user.getId(),
                request.getCoverLetter()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/user")
    public ResponseEntity<List<JobApplicationDto>> getUserApplications(@AuthenticationPrincipal User user) {
        List<JobApplicationDto> applications = jobApplicationService.getUserApplications(user.getId());
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/company")
    public ResponseEntity<List<JobApplicationDto>> getCompanyApplications(@AuthenticationPrincipal Company company) {
        List<JobApplicationDto> applications = jobApplicationService.getCompanyApplications(company.getId());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplicationDto>> getJobApplications(@PathVariable Long jobId) {
        List<JobApplicationDto> applications = jobApplicationService.getJobApplications(jobId);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<JobApplication> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {
        JobApplication application = jobApplicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(application);
    }
}