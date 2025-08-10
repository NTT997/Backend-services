package com.salesmanager.core.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.repositories.JobScheduleConfig.JobScheduleConfigRepository;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ScheduleConfigService {
    
    @Autowired
    private JobScheduleConfigRepository scheduleRepository;
    
    // Existing methods
    public JobScheduleConfig getScheduleConfig(String jobName) {
        return scheduleRepository.findByJobName(jobName);
    }
    
    public JobScheduleConfig saveOrUpdateScheduleConfig(String jobName, String cronExpression, Boolean enabled) {
        JobScheduleConfig config = scheduleRepository.findByJobName(jobName);
        if (config == null) {
            config = new JobScheduleConfig(jobName, cronExpression, enabled);
        } else {
            config.setCronExpression(cronExpression);
            config.setEnabled(enabled);
        }
        return scheduleRepository.save(config);
    }
    
    public JobScheduleConfig saveScheduleConfig(JobScheduleConfig config) {
        return scheduleRepository.save(config);
    }
    
    // New methods for multiple jobs
    public List<JobScheduleConfig> getAllEnabledJobs() {
        return scheduleRepository.findAllEnabledJobs();
    }
    
    public List<JobScheduleConfig> getAllJobs() {
        return scheduleRepository.findAllOrderByJobName();
    }
    
    public List<String> getEnabledJobNames() {
        return scheduleRepository.findEnabledJobNames();
    }
    
    public Long getEnabledJobCount() {
        return scheduleRepository.countEnabledJobs();
    }
    
    // Execution tracking methods
    public void updateExecutionInfo(String jobName, Date executionTime, String status, Long durationMs) {
        scheduleRepository.updateExecutionInfo(jobName, executionTime, status, durationMs);
    }
    
    public void updateExecutionStatus(String jobName, String status) {
        scheduleRepository.updateExecutionStatus(jobName, status);
    }
    
    // Bulk operations
    public void enableAllJobs() {
        List<JobScheduleConfig> allJobs = scheduleRepository.findAll();
        allJobs.forEach(job -> job.setEnabled(true));
        scheduleRepository.saveAll(allJobs);
    }
    
    public void disableAllJobs() {
        List<JobScheduleConfig> allJobs = scheduleRepository.findAll();
        allJobs.forEach(job -> job.setEnabled(false));
        scheduleRepository.saveAll(allJobs);
    }
}