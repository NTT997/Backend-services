package com.salesmanager.core.business.services.multijobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.MediaType;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.remote_inventory.RemoteProductInventoryService;
import com.salesmanager.core.business.services.system.ScheduleConfigService;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


import org.kie.soup.commons.util.Lists;
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
    
    @Autowired
    private RemoteProductInventoryService remoteProductInventoryService;

    // Store multiple scheduled tasks
    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private Map<String, String> currentCronExpressions = new ConcurrentHashMap<>();
    
    // Define all your jobs here
    private static final List<JobDefinition> JOB_DEFINITIONS = Arrays.asList(
//        new JobDefinition("order", "0 0 2 * * ?", true),           // 2AM daily
//        new JobDefinition("cleanup", "0 */5 * * * ?", true),       // Every 5 minutes
        new JobDefinition("inventory", "0 */1 * * * ?", true),       // Every 5 minutes,
//        new JobDefinition("backup", "0 0 1 * * ?", true),          // 1AM daily
        new JobDefinition("pull_product_job", "0 0 */6 * * ?", true)         // Every 6 hours
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
                    case "inventory":
                        performInventoryJob();
                        break;
                    case "cleanup":
                        performCleanupJob();
                        break;
                    case "backup":
                        performBackupJob();
                        break;
                    case "pull_product_job":
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
    
//    // Individual job execution methods
//    private void performInventoryJob() throws ServiceException {
//        logger.info("Performing inventory sync processing job...");
//        System.out.println("Performing inventory processing job...");
//        try {
//            Thread.sleep(2000); // Simulate order processing work
//            System.out.println("Inventory processing job completed");
//            List<ProductAvailability>productAvailabilities = new ArrayList<>();
//            ProductAvailability prod1 = new ProductAvailability(1,30,1);
//            RestTemplate restTemplate = new RestTemplate();//use for external call
//            
//            //ProductAvailability prod2 = new ProductAvailability(250,50,250);
//            //api/v1/public/remote-availability/sync-data
//            //productAvailabilities.add(prod2);
//            productAvailabilities.add(prod1);
//            remoteProductInventoryService.syncDataFromLocalToRemoteService(productAvailabilities);
//            logger.info("Inventory processing job completed");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.out.println("Inventory job was interrupted");
//            logger.warn("Inventory job was interrupted");
//        }
//    }
 // Individual job execution methods
    private void performInventoryJob() throws ServiceException {
        logger.info("Performing inventory sync processing job...");
        System.out.println("Performing inventory processing job...");
        try {
            Thread.sleep(2000); // Simulate order processing work
            System.out.println("Inventory processing job completed");

            // Prepare data
            List<ProductAvailability> productAvailabilities = new ArrayList<>();
            ProductAvailability prod1 = new ProductAvailability(1, 30, 1);
            productAvailabilities.add(prod1);

            // REST call setup
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://localhost:8080/api/v1/public/remote-availability/sync-data";

            // Wrap the request
            HttpEntity requestEntity = new HttpEntity(productAvailabilities);

            // Send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            // Log result
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Inventory sync POST request successful: " + response.getBody());
            } else {
                logger.warn("Inventory sync POST request failed: " + response.getStatusCode());
            }

            logger.info("Inventory processing job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Inventory job was interrupted");
            logger.warn("Inventory job was interrupted");
        } catch (Exception e) {
            logger.error("Error performing inventory job", e);
        }
    }

    private List<ProductAvailability> List() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'List'");
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
        logger.info("Performing pull product generation job...");
        System.out.println("Performing pull product generation job...");
        try {
            Thread.sleep(2000); // Simulate order processing work
            System.out.println("Inventory processing job completed");

            // Prepare data
            List<ProductAvailability> productAvailabilities = new ArrayList<>();
            ProductAvailability prod1 = new ProductAvailability(1, 30, 1);
            productAvailabilities.add(prod1);

            // REST call setup
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://www.cakewaibackend.id.vn/api/public/products";

            // Send GET request
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl,String.class);

            // Log result
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("pull product successfully: " + response.getBody());
                System.out.println("\n==============================================================================\n");
                System.out.println(response.getBody());
                System.out.println("\n==============================================================================\n");
            } else {
                logger.warn("Inventory sync GET request failed: " + response.getStatusCode());
            }

            logger.info("Products pulling job completed");
            System.out.println("Products pulling job completed");
            logger.info("Products pulling job completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Pulling job was interrupted");
            logger.warn("Pulling job was interrupted");
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
    private static final Map<String, String> CRON_MAP = new HashMap<>();

    static {
        CRON_MAP.put("0 0 * * * ?", "Every hour");
        CRON_MAP.put("0 0 0 * * ?", "Every day at midnight");
        CRON_MAP.put("0 0 12 * * ?", "Every day at noon");
        CRON_MAP.put("0 0 0 ? * MON", "Every Monday at midnight");
        CRON_MAP.put("0 0 0 1 * ?", "First day of every month at midnight");
        // Add as many mappings as you want
    }

    public static String toHumanReadable(String cron) {
        return CRON_MAP.getOrDefault(cron, cron);
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