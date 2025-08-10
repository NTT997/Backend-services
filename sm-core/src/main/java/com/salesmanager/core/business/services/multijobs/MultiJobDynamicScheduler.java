package com.salesmanager.core.business.services.multijobs;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.salesmanager.core.business.services.system.ScheduleConfigService;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MultiJobDynamicScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiJobDynamicScheduler.class);
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    @Autowired
    private ScheduleConfigService scheduleConfigService;
    
    // Store multiple scheduled tasks
    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private Map<String, String> currentCronExpressions = new ConcurrentHashMap<>();
    
    // Define all your jobs here
    private static final List<JobDefinition> JOB_DEFINITIONS = Arrays.asList(
        new JobDefinition("order", "0 0 2 * * ?", true),           // 2AM daily
        new JobDefinition("cleanup", "0 */5 * * * ?", true),       // Every 5 minutes
        new JobDefinition("backup", "0 0 1 * * ?", true),          // 1AM daily
        new JobDefinition("report", "0 0 */6 * * ?", true)         // Every 6 hours
    );
    
    @PostConstruct
    public void initializeScheduler() {
        logger.info("Initializing multi-job scheduler...");
        
        // Initialize all defined jobs
        for (JobDefinition jobDef : JOB_DEFINITIONS) {
            scheduleJob(jobDef.getName(), jobDef.getDefaultCron(), jobDef.isDefaultEnabled());
        }
        
        logger.info("Multi-job scheduler initialized with {} jobs", JOB_DEFINITIONS.size());
    }
    
    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up all scheduled tasks...");
        scheduledTasks.values().forEach(task -> {
            if (task != null) {
                task.cancel(false);
            }
        });
        scheduledTasks.clear();
    }
    
    @Scheduled(fixedRate = 100000) // Check every ~1.7 minutes
    public void checkForScheduleUpdates() {
        logger.debug("Checking for schedule updates...");
        
        for (JobDefinition jobDef : JOB_DEFINITIONS) {
            String jobName = jobDef.getName();
            JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
            
            if (config != null) {
                String newCronExpression = config.getCronExpression();
                boolean isEnabled = config.getEnabled();
                String currentCron = currentCronExpressions.get(jobName);
                ScheduledFuture<?> currentTask = scheduledTasks.get(jobName);
                
                // Check if schedule changed
                if (!newCronExpression.equals(currentCron)) {
                    logger.info("Schedule updated for {}: {} -> {}", jobName, currentCron, newCronExpression);
                    rescheduleJob(jobName, newCronExpression);
                }
                
                // Handle enable/disable status
                if (isEnabled && currentTask == null) {
                    // Job was disabled but now enabled - start it
                    logger.info("Job {} was re-enabled, starting scheduler", jobName);
                    System.out.println("Job " + jobName + " was re-enabled, starting scheduler");
                    scheduleWithCron(jobName, currentCronExpressions.get(jobName));
                } else if (!isEnabled && currentTask != null) {
                    // Job was enabled but now disabled - stop it
                    logger.info("Job {} was disabled, stopping scheduler", jobName);
                    System.out.println("Job " + jobName + " was disabled, stopping scheduler");
                    currentTask.cancel(false);
                    scheduledTasks.remove(jobName);
                }
            }
        }
    }
    
    // Generic job execution method that determines which job to run
    @Transactional(readOnly = true)
    public void executeJob(String jobName) {
        try {
            logger.info("Starting scheduled job execution for {}: {}", jobName, new Date());
            System.out.printf("Starting scheduled job execution for %s%n", jobName);
            
            // Get current config from database
            JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
            
            // Check database enable flag
            if (config != null && config.getEnabled()) {
                
                // Route to appropriate job execution method based on job name
                switch (jobName.toLowerCase()) {
                    case "order":
                        performOrderJob();
                        break;
                    case "cleanup":
                        performCleanupJob();
                        break;
                    case "backup":
                        performBackupJob();
                        break;
                    case "report":
                        performReportJob();
                        break;
                    default:
                        logger.warn("Unknown job name: {}", jobName);
                        return;
                }
                
                System.out.printf("Job %s execution completed successfully%n", jobName);
                logger.info("Job {} execution completed successfully at: {}", jobName, new Date());
            } else {
                System.out.printf("Job %s execution skipped - job is disabled in database%n", jobName);
                logger.info("Job {} execution skipped - job is disabled in database", jobName);
            }
            
        } catch (Exception e) {
            System.out.printf("Error during job %s execution%n", jobName);
            logger.error("Error during job {} execution", jobName, e);
        }
    }
    
    // Individual job execution methods
    private void performOrderJob() {
        logger.info("Performing order processing job...");
        System.out.println("Performing order processing job...");
        try {
            Thread.sleep(2000); // Simulate order processing work
            System.out.println("Order processing job completed");
            logger.info("Order processing job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Order job was interrupted");
            logger.warn("Order job was interrupted");
        }
    }
    
    private void performCleanupJob() {
        logger.info("Performing cleanup job...");
        System.out.println("Performing cleanup job...");
        try {
            Thread.sleep(500); // Simulate cleanup work
            System.out.println("Cleanup job completed");
            logger.info("Cleanup job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cleanup job was interrupted");
            logger.warn("Cleanup job was interrupted");
        }
    }
    
    private void performBackupJob() {
        logger.info("Performing backup job...");
        System.out.println("Performing backup job...");
        try {
            Thread.sleep(3000); // Simulate backup work
            System.out.println("Backup job completed");
            logger.info("Backup job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Backup job was interrupted");
            logger.warn("Backup job was interrupted");
        }
    }
    
    private void performReportJob() {
        logger.info("Performing report generation job...");
        System.out.println("Performing report generation job...");
        try {
            Thread.sleep(1500); // Simulate report generation work
            System.out.println("Report generation job completed");
            logger.info("Report generation job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Report job was interrupted");
            logger.warn("Report job was interrupted");
        }
    }
    
    private void scheduleJob(String jobName, String defaultCron, boolean defaultEnabled) {
        JobScheduleConfig config = scheduleConfigService.getScheduleConfig(jobName);
        String cronExpression;
        boolean enabled;
        
        if (config != null) {
            cronExpression = config.getCronExpression();
            enabled = config.getEnabled();
        } else {
            cronExpression = defaultCron;
            enabled = defaultEnabled;
            // Save default configuration to database
            scheduleConfigService.saveOrUpdateScheduleConfig(jobName, cronExpression, enabled);
        }
        
        currentCronExpressions.put(jobName, cronExpression);
        
        if (enabled) {
            scheduleWithCron(jobName, cronExpression);
        }
        
        logger.info("Job {} initialized with cron: {}, enabled: {}", jobName, cronExpression, enabled);
    }
    
    private void rescheduleJob(String jobName, String newCronExpression) {
        ScheduledFuture<?> currentTask = scheduledTasks.get(jobName);
        if (currentTask != null) {
            currentTask.cancel(false);
        }
        
        currentCronExpressions.put(jobName, newCronExpression);
        scheduleWithCron(jobName, newCronExpression);
    }
    
    private void scheduleWithCron(String jobName, String cronExpression) {
        try {
            CronTrigger trigger = new CronTrigger(cronExpression);
            ScheduledFuture<?> task = taskScheduler.schedule(() -> executeJob(jobName), trigger);
            scheduledTasks.put(jobName, task);
            
            System.out.printf("Job %s scheduled with cron expression: %s%n", jobName, cronExpression);
            logger.info("Job {} scheduled with cron expression: {}", jobName, cronExpression);
        } catch (Exception e) {
            System.out.printf("Failed to schedule job %s with cron expression: %s%n", jobName, cronExpression);
            logger.error("Failed to schedule job {} with cron expression: {}", jobName, cronExpression, e);
        }
    }
    
    // Helper method to get all active jobs
    public Map<String, String> getActiveJobs() {
        Map<String, String> activeJobs = new ConcurrentHashMap<>();
        scheduledTasks.forEach((jobName, task) -> {
            if (task != null && !task.isCancelled()) {
                activeJobs.put(jobName, currentCronExpressions.get(jobName));
            }
        });
        return activeJobs;
    }
    
    // Helper method to check if a specific job is running
    public boolean isJobActive(String jobName) {
        ScheduledFuture<?> task = scheduledTasks.get(jobName);
        return task != null && !task.isCancelled();
    }
    
    // Inner class for job definition
    private static class JobDefinition {
        private final String name;
        private final String defaultCron;
        private final boolean defaultEnabled;
        
        public JobDefinition(String name, String defaultCron, boolean defaultEnabled) {
            this.name = name;
            this.defaultCron = defaultCron;
            this.defaultEnabled = defaultEnabled;
        }
        
        public String getName() { return name; }
        public String getDefaultCron() { return defaultCron; }
        public boolean isDefaultEnabled() { return defaultEnabled; }
    }
}