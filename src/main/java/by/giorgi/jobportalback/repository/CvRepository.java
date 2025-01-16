package by.giorgi.jobportalback.repository;


import by.giorgi.jobportalback.model.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CvRepository extends JpaRepository<Cv, Long> {

}
