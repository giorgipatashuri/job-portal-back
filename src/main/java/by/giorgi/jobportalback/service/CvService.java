package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.mapper.CvMapper;
import by.giorgi.jobportalback.mapper.CvMapperImpl;
import by.giorgi.jobportalback.model.dto.CvDto;
import by.giorgi.jobportalback.model.entity.Cv;
import by.giorgi.jobportalback.model.entity.Skill;
import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.repository.CvRepository;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CvService {

    private final CvRepository cvRepository;

    private final UserRepository userRepository ;
    private final CvMapperImpl cvMapper;

    public CvDto createCv(CvDto cvDto,Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));

        Cv cv = cvMapper.cvDtoToCV(cvDto);
        cv.setUser(user);

        if(cv.getSkills()!=null) {
            cv.getSkills().forEach(skill -> skill.setCv(cv));
        }
        if(cv.getExperiences()!=null) {
            cv.getExperiences().forEach(experience -> experience.setCv(cv));
        }
        if(cv.getEducation()!=null) {
            cv.getEducation().forEach(Education -> Education.setCv(cv));
        }
        Cv savedCv = cvRepository.save(cv);
        return cvMapper.cvToCVDto(savedCv);
    }

}
