package com.salesmanager.core.model.logging;


import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "DATA_LOG")
public class DbLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "db_log_seq")
    @SequenceGenerator(name = "db_log_seq", sequenceName = "SEQ_AUTO_INCREMENT", allocationSize = 1)
    @Column(name = "LOGID")
    private Long logId;

    @Column(name = "METHOD", length = 1024)
    private String method;

    @Column(name = "LOG_MESSAGE", length = 1024)
    private String logMessage;

    @Column(name = "LOG_USERID_FK", length = 1024)
    private String logUserId;

    @Column(name = "LOG_IP_ADDRESS", length = 1024)
    private String logIpAddress;


    @Column(name = "LOG_TIME")
    private LocalDateTime logTime;

    @Column(name = "LOG_MENU", length = 50)
    private String logMenu;

    @Column(name = "MESSAGE_TEMPLATE", length = 1024)
    private String messageTemplate;

    @Column(name = "LOGLEVEL", length = 1024)
    private String logLevel;

    @Column(name = "EXCEPTION", length = 1024)
    private String exception;

    @Column(name = "PROPERTIES", length = 1024)
    private String properties;

    // Constructors
    public DbLog() {
    }

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogUserId() {
        return logUserId;
    }

    public void setLogUserId(String logUserId) {
        this.logUserId = logUserId;
    }

    public String getLogIpAddress() {
        return logIpAddress;
    }

    public void setLogIpAddress(String logIpAddress) {
        this.logIpAddress = logIpAddress;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime localDateTime) {
        this.logTime = localDateTime;
    }

    public String getLogMenu() {
        return logMenu;
    }

    public void setLogMenu(String logMenu) {
        this.logMenu = logMenu;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}

