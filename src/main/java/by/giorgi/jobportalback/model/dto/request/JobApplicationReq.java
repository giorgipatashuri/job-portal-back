package by.giorgi.jobportalback.model.dto.request;

import lombok.Data;

@Data
public class JobApplicationReq {
    private Long jobId;
    private Long cvId;
    private String coverLetter;
}
