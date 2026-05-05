package com.example.ResumeAnalyzerPro_Final.repository;

import com.example.ResumeAnalyzerPro_Final.entity.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {

    Optional<JobDetail> findByJobRole(String jobRole);

    boolean existsByJobRole(String jobRole);
}
