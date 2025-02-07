package by.giorgi.jobportalback.controller;

import by.giorgi.jobportalback.model.dto.request.JobCreateReq;
import by.giorgi.jobportalback.model.entity.Company;
import by.giorgi.jobportalback.model.entity.Job;
import by.giorgi.jobportalback.repository.CompanyRepository;
import by.giorgi.jobportalback.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody JobCreateReq jobRequest, @AuthenticationPrincipal UserDetails userDetails ) {
        Company company = companyRepository.findByCompanyEmail(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("Company not found"));

        Job job = new Job();
        job.setJobName(jobRequest.getTitle());
        job.setJobDescription(jobRequest.getDescription());
        job.setLocation(jobRequest.getLocation());
        job.setSalary(jobRequest.getSalary());
        job.setRequirements(jobRequest.getRequirements());
        job.setType(jobRequest.getType());
        job.setCompany(company);

        Job savedJob = jobRepository.save(job);
        return ResponseEntity.ok(savedJob);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobRepository.findAll());
    }
    @GetMapping("/company/get-all")
    public ResponseEntity<List<Job>> getAllCompanyJobs(@AuthenticationPrincipal UserDetails userDetails) {
        Company company = companyRepository.findByCompanyEmail(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("Company not found"));
        return ResponseEntity.ok(jobRepository.findByCompanyId(company.getId()));
    }
}
