package by.giorgi.jobportalback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String phone;
    private String email;
    private List<SkillDTO> skills;
    private List<ExperienceDTO> experiences;
    private List<EducationDTO> education;
}

