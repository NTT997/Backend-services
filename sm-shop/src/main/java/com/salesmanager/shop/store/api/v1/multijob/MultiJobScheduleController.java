// Enhanced REST Controller for managing multiple jobs
package com.salesmanager.shop.store.api.v1.multijob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.support.CronTrigger;

import com.salesmanager.core.business.services.multijobs.MultiJobDynamicScheduler;
import com.salesmanager.core.business.services.system.ScheduleConfigService;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;


import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedules")
public class MultiJobScheduleController {
    
    @Autowired
    private ScheduleConfigService scheduleConfigService;
    
    @Autowired
    private MultiJobDynamicScheduler jobScheduler;
    
    // Get all jobs configuration
    @GetMapping
    public ResponseEntity<List<JobScheduleConfig>> getAllSchedules() {
        List<JobScheduleConfig> configs = scheduleConfigService.getAllJobs();
        return ResponseEntity.ok(configs);
    }
    
    // Get specific job configuration
    @GetMapping("/{jobName}")
    public ResponseEntity<JobScheduleConfig> getScheduleConfig(@PathVariable String jobName) {
        JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
        return config != null ? ResponseEntity.ok(config) : ResponseEntity.notFound().build();
    }
    
    // Update job schedule and enable status
    @GetMapping("/update/{jobName}")
    public ResponseEntity<JobScheduleConfig> updateScheduleConfig(
            @PathVariable String jobName,
            @RequestParam String cronExpression,
            @RequestParam Boolean enabled) {
        
        try {
            // Validate cron expression
            new CronTrigger(cronExpression);
            System.out.println("PRINT LINE 52 AT CONTROLLER");
            JobScheduleConfig config = scheduleConfigService.saveOrUpdateScheduleConfig(
                jobName, cronExpression, enabled);
            
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
   // Toggle job enable/disable status only
   @PutMapping("/{jobName}/toggle")
   public ResponseEntity<JobScheduleConfig> toggleJobStatus(
           @PathVariable String jobName,
           @RequestParam Boolean enabled) {
       
       JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
       if (config == null) {
           return ResponseEntity.notFound().build();
       }
       
       config.setEnabled(enabled);
       config.setLastUpdated(new Date());
       JobScheduleConfig updatedConfig = scheduleConfigService.saveScheduleConfig(config);
       
       return ResponseEntity.ok(updatedConfig);
   }
    
    // Get currently active jobs status
    @GetMapping("/active")
    public ResponseEntity<Map<String, String>> getActiveJobs() {
        Map<String, String> activeJobs = jobScheduler.getActiveJobs();
        return ResponseEntity.ok(activeJobs);
    }
    
    // Check if specific job is active
    @GetMapping("/{jobName}/status")
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable String jobName) {
        boolean isActive = jobScheduler.isJobActive(jobName);
        JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
        
        Map<String, Object> status = Map.of(
            "jobName", jobName,
            "isActive", isActive,
            "isEnabled", config != null ? config.getEnabled() : false,
            "cronExpression", config != null ? config.getCronExpression() : "Not configured"
        );
        
        return ResponseEntity.ok(status);
    }
}