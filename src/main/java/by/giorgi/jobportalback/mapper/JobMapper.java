package by.giorgi.jobportalback.mapper;

import by.giorgi.jobportalback.model.dto.*;
import by.giorgi.jobportalback.model.entity.Job;
import by.giorgi.jobportalback.model.entity.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobApplicationDto toDto(JobApplication application) {
        if (application == null) {
            return null;
        }

        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(application.getId());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        // Map Job
        if (application.getJob() != null) {
            JobDto jobDto = new JobDto();
            Job job = application.getJob();
            jobDto.setId(job.getId());
            jobDto.setJobName(job.getJobName());
            jobDto.setJobDescription(job.getJobDescription());
            jobDto.setLocation(job.getLocation());
            jobDto.setSalary(job.getSalary());
            jobDto.setType(String.valueOf(job.getType()));

            // Map Company
            if (job.getCompany() != null) {
                CompanyBriefDto companyDto = new CompanyBriefDto();
                companyDto.setId(job.getCompany().getId());
                companyDto.setCompanyName(job.getCompany().getCompanyName());
                companyDto.setCompanyEmail(job.getCompany().getCompanyEmail());
                jobDto.setCompany(companyDto);
            }

            dto.setJob(jobDto);
        }

        // Map CV
        if (application.getCv() != null) {
            CvDto cvDto = new CvDto();
            cvDto.setId(application.getCv().getId());
            cvDto.setSummary(application.getCv().getSummary());
            dto.setCv(cvDto);
        }

        // Map User
        if (application.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(application.getUser().getId());
            userDto.setName(application.getUser().getName());
            userDto.setLastname(application.getUser().getLastname());
            userDto.setEmail(application.getUser().getEmail());
            dto.setUser(userDto);
        }

        return dto;
    }
}