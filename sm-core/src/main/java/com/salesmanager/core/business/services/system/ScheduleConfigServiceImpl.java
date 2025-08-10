package com.salesmanager.core.business.services.system;

import java.util.List;

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

    @Override
    public JobScheduleConfig getScheduleConfig(String jobName) {
        return jobScheduleRepository.findByJobName(jobName);
    }

    @Override
    public JobScheduleConfig saveOrUpdateScheduleConfig(String jobName, String cronExpression,
            Boolean enabled) {
        JobScheduleConfig config = jobScheduleRepository.findByJobName(jobName);
        if (config == null) {
            config = new JobScheduleConfig(jobName, cronExpression, enabled);
        } else {
            config.setCronExpression(cronExpression);
            config.setEnabled(enabled);
            config.setLastUpdated(null);
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
