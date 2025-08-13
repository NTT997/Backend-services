package com.salesmanager.core.business.services.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.JobScheduleConfig.JobScheduleConfigRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;

@Service
@Transactional
public class ScheduleConfigServiceImpl extends SalesManagerEntityServiceImpl<Long, JobScheduleConfig> implements ScheduleConfigService{

    private JobScheduleConfigRepository jobScheduleRepository;
    public ScheduleConfigServiceImpl(JobScheduleConfigRepository repository) {
        super(repository);
        jobScheduleRepository = repository;
    }
    private static final Map<String, String> CRON_MAP = new HashMap<>();
    static {
        CRON_MAP.put("0 0 2 * * ?", "2 AM daily");
        CRON_MAP.put("0 */5 * * * ?", "Every 5 minutes");
        CRON_MAP.put("0 */1 * * * ?", "Every 1 minute");
        CRON_MAP.put("0 0 1 * * ?", "1 AM daily");
        CRON_MAP.put("0 0 */6 * * ?", "Every 6 hours");
    }
    public static String toHumanReadable(String cron) {
        return CRON_MAP.getOrDefault(cron, cron);
    }
    @Override
    public JobScheduleConfig getScheduleConfig(String jobName) {
        return jobScheduleRepository.findByJobName(jobName);
    }

    @Override
    public JobScheduleConfig saveOrUpdateScheduleConfig(String jobName, String cronExpression,
            Boolean enabled) {
        JobScheduleConfig config = jobScheduleRepository.findByJobName(jobName);
        
        if (config == null) {
        	System.out.println("PRINT LINE 34 OF MULTIJOB");
            config = new JobScheduleConfig(jobName, cronExpression, enabled);
            System.out.println(cronExpression);
            config.setDescription(toHumanReadable(cronExpression));
        } else {
        	System.out.println("PRINT LINE 34 OF MULTIJOB");
            config.setCronExpression(cronExpression);
            config.setEnabled(enabled);
            config.setLastUpdated(null);
            config.setDescription(toHumanReadable(cronExpression));
        }
        return jobScheduleRepository.save(config);
    }

    @Override
    public List<JobScheduleConfig> getAllEnabledJobs() {
        return jobScheduleRepository.findAllEnabledJobs();
    }

    @Override
    public Long getEnabledJobCount() {
        return jobScheduleRepository.countEnabledJobs();
    }

    @Override
    public JobScheduleConfig saveScheduleConfig(JobScheduleConfig config) {
        return jobScheduleRepository.save(config);
    }
    @Override
    public List<JobScheduleConfig> getAllJobs() {
        return jobScheduleRepository.findAll();
    }
    // @Override
    // public void disableAllJobs() {
    //     return jobScheduleRepository.de
    // }

    // @Override
    // public void enableAllJobs() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'enableAllJobs'");
    // }
    
}
