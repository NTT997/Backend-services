package com.salesmanager.core.business.services.system;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.JobScheduleConfig.JobScheduleConfigRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.jobscheduleconfig.JobScheduleConfig;
import com.salesmanager.core.model.system.SystemConfiguration;

import java.util.List;

// Service class for managing schedule configurations

public interface ScheduleConfigService extends SalesManagerEntityService<Long, JobScheduleConfig>{
    
    public JobScheduleConfig getScheduleConfig(String jobName);
    
    public JobScheduleConfig saveOrUpdateScheduleConfig(String jobName, String cronExpression, Boolean enabled);
    
    public List<JobScheduleConfig> getAllEnabledJobs();

    public Long getEnabledJobCount();

    public JobScheduleConfig saveScheduleConfig(JobScheduleConfig config);

    // public void disableAllJobs();

    // public void enableAllJobs();
}

