package by.giorgi.jobportalback.repository;

import by.giorgi.jobportalback.model.entity.Job;
import by.giorgi.jobportalback.model.entity.JobApplication;
import by.giorgi.jobportalback.model.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
//    @Query("SELECT DISTINCT ja. FROM JobApplication ja " +
//            "WHERE ja.job.company.id = :companyId")
//    List<User> findAllApplicantsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT ja FROM JobApplication ja " +
            "WHERE ja.job.company.id = :companyId")
    List<JobApplication> findAllApplicationsByCompanyId(@Param("companyId") Long companyId);
    List<Job> findByCompanyId(Long companyId);
}
