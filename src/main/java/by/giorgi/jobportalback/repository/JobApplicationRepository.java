package by.giorgi.jobportalback.repository;

import by.giorgi.jobportalback.model.entity.JobApplication;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserId(Long userId);
//    List<JobApplication> findByCompanyId(Long userId);

    List<JobApplication> findByJobId(Long jobId);
    Optional<JobApplication> findByJobIdAndCvId(Long jobId, Long cvId);
    boolean existsByJobIdAndCvId(Long jobId, Long cvId);


    @Modifying
    @Transactional
    @Query("DELETE FROM JobApplication j WHERE j.cv.id = :cvId")
    void deleteByCvId(@Param("cvId") Long cvId);
}
