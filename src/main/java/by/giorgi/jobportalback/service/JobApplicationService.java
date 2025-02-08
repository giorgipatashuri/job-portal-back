package by.giorgi.jobportalback.service;

import by.giorgi.jobportalback.mapper.JobMapper;
import by.giorgi.jobportalback.model.dto.JobApplicationDto;
import by.giorgi.jobportalback.model.entity.Cv;
import by.giorgi.jobportalback.model.entity.Job;
import by.giorgi.jobportalback.model.entity.JobApplication;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.ApplicationStatus;
import by.giorgi.jobportalback.repository.CvRepository;
import by.giorgi.jobportalback.repository.JobApplicationRepository;
import by.giorgi.jobportalback.repository.JobRepository;
import by.giorgi.jobportalback.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final CvRepository cvRepository;
    private final UserRepository userRepository;
    private final JobMapper jobApplicationMapper;

    public JobApplication applyForJob(Long jobId, Long cvId, Long userId, String coverLetter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        Cv cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setCv(cv);
        application.setUser(user);
        application.setCoverLetter(coverLetter);
        application.setStatus(ApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        return jobApplicationRepository.save(application);
    }

    public List<JobApplicationDto> getUserApplications(Long userId) {
        List<JobApplication> applications = jobApplicationRepository.findByUserId(userId);
        return applications.stream()
                .map(jobApplicationMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<JobApplicationDto> getCompanyApplications(Long companyId) {
        List<JobApplication> applications = jobRepository.findAllApplicationsByCompanyId(companyId);
        return applications.stream()
                .map(jobApplicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDto> getJobApplications(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobId(jobId);
        return applications.stream()
                .map(jobApplicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        return jobApplicationRepository.save(application);
    }
}
