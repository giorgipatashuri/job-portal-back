package by.giorgi.jobportalback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String summary;
    private List<SkillDto> skills;
    private List<ExperienceDto> experiences;
    private List<EducationDto> education;
}

