package by.giorgi.jobportalback.repository;


import by.giorgi.jobportalback.model.dto.CvDto;
import by.giorgi.jobportalback.model.entity.Cv;
import by.giorgi.jobportalback.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CvRepository extends JpaRepository<Cv, Long> {
    List<Cv> findByUser(User user);
}
