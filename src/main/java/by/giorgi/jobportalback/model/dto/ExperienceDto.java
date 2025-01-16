package by.giorgi.jobportalback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDto {
    private Long id;
    private String title;
    private String companyName;
    private String city;
    private String startDate;
    private String endDate;
    private Boolean currentlyWorking;
    private String workSummary;
}
