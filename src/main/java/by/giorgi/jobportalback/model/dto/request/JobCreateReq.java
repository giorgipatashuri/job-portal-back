package by.giorgi.jobportalback.model.dto.request;

import by.giorgi.jobportalback.model.enums.JobType;
import lombok.Data;

@Data
public class JobCreateReq {
    private String title;
    private String description;
    private String location;
    private String salary;
    private String requirements;
    private JobType type;
}
