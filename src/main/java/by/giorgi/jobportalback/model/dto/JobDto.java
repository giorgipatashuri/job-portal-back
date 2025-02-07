package by.giorgi.jobportalback.model.dto;

import lombok.Data;

@Data
public class JobDto {
    private Long id;
    private String jobName;
    private String jobDescription;
    private String location;
    private String salary;
    private String requirements;
    private String type;
    private CompanyBriefDto company;
}

