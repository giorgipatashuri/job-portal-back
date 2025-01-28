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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public CvDto getCvById(Long id) {
        Cv cv = cvRepository.findById(id).orElseThrow(()-> new RuntimeException("Cv not found"));
        return cvMapper.cvToCVDto(cv);
    }
    public List<CvDto> getAllUserCvs(UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
        List<Cv> cvs = cvRepository.findByUser(user);
        return cvMapper.cvsToCvDtos(cvs);
    }
    public void deleteCvById(Long id, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cv cv = cvRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CV not found"));

        if (!cv.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You do not own this CV");
        }
        cvRepository.deleteById(id);
    }
}
