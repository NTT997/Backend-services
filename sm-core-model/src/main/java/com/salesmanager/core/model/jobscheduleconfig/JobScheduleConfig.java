package com.salesmanager.core.model.jobscheduleconfig;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "SCHEDULE_JOB")
public class JobScheduleConfig extends SalesManagerEntity<Long, JobScheduleConfig> {

        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", unique = true, nullable = false)
    private String jobName;
    
    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled = true;
    
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    
    // Optional additional fields for enhanced functionality
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "last_execution")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastExecution;
    
    @Column(name = "execution_count")
    private Long executionCount = 0L;
    
    @Column(name = "last_execution_status")
    private String lastExecutionStatus;
    
    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;
    
    // Constructors
    public JobScheduleConfig() {
        this.createdDate = new Date();
        this.lastUpdated = new Date();
    }
    
    public JobScheduleConfig(String jobName, String cronExpression, Boolean enabled) {
        this();
        this.jobName = jobName;
        this.cronExpression = cronExpression;
        this.enabled = enabled;
    }
    
    public JobScheduleConfig(String jobName, String cronExpression, Boolean enabled, String description) {
        this(jobName, cronExpression, enabled);
        this.description = description;
    }
    
    // Update last updated timestamp before persist/update
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = new Date();
        if (this.createdDate == null) {
            this.createdDate = new Date();
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public Date getLastExecution() { return lastExecution; }
    public void setLastExecution(Date lastExecution) { this.lastExecution = lastExecution; }
    
    public Long getExecutionCount() { return executionCount; }
    public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }
    
    public String getLastExecutionStatus() { return lastExecutionStatus; }
    public void setLastExecutionStatus(String lastExecutionStatus) { this.lastExecutionStatus = lastExecutionStatus; }
    
    public Long getExecutionDurationMs() { return executionDurationMs; }
    public void setExecutionDurationMs(Long executionDurationMs) { this.executionDurationMs = executionDurationMs; }
    
    @Override
    public String toString() {
        return String.format("JobScheduleConfig{jobName='%s', cronExpression='%s', enabled=%s}", 
                           jobName, cronExpression, enabled);
    }
    
}
