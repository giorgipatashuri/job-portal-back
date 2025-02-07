package by.giorgi.jobportalback.model.dto;

import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.model.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationDto {
    private Long id;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
    private JobDto job;
    private CvDto cv;
    private UserDto user;
}
