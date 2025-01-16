package by.giorgi.jobportalback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {
    private Long id;
    private String universityName;
    private String degree;
    private String major;
    private String startDate;
    private String endDate;
    private String description;
}
