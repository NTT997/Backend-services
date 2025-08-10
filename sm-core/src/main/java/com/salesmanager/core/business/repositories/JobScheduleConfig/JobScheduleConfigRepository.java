package com.salesmanager.core.business.repositories.JobScheduleConfig;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;

// import java.util.List;


// @Repository
// public interface JobScheduleConfigRepository extends JpaRepository<JobScheduleConfig,Long>{
//     JobScheduleConfig findByJobName(String jobName);
    
//     @Query("SELECT j FROM JobScheduleConfig j WHERE j.enabled = true")
//     List<JobScheduleConfig> findAllEnabledJobs(); 
// }

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;
import java.util.List;
import java.util.Date;

@Repository
public interface JobScheduleConfigRepository extends JpaRepository<JobScheduleConfig, Long> {
    
    // Existing methods
    JobScheduleConfig findByJobName(String jobName);
    
    @Query("SELECT j FROM JobScheduleConfig j WHERE j.enabled = true")
    List<JobScheduleConfig> findAllEnabledJobs();
    
    // Additional useful methods for multiple jobs
    List<JobScheduleConfig> findAllByEnabled(Boolean enabled);
    
    @Query("SELECT j FROM JobScheduleConfig j ORDER BY j.jobName")
    List<JobScheduleConfig> findAllOrderByJobName();
    
    @Query("SELECT COUNT(j) FROM JobScheduleConfig j WHERE j.enabled = true")
    Long countEnabledJobs();
    
    @Query("SELECT j.jobName FROM JobScheduleConfig j WHERE j.enabled = true")
    List<String> findEnabledJobNames();
    
    // Methods to update execution tracking
    @Modifying
    @Transactional
    @Query("UPDATE JobScheduleConfig j SET j.lastExecution = :executionTime, " +
           "j.executionCount = j.executionCount + 1, j.lastExecutionStatus = :status, " +
           "j.executionDurationMs = :duration WHERE j.jobName = :jobName")
    int updateExecutionInfo(@Param("jobName") String jobName, 
                           @Param("executionTime") Date executionTime,
                           @Param("status") String status,
                           @Param("duration") Long duration);
    
    @Modifying
    @Transactional
    @Query("UPDATE JobScheduleConfig j SET j.lastExecutionStatus = :status WHERE j.jobName = :jobName")
    int updateExecutionStatus(@Param("jobName") String jobName, @Param("status") String status);
}