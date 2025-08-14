package com.salesmanager.core.business.services.log;

import com.salesmanager.core.business.repositories.log.DbLogRepository;
import com.salesmanager.core.model.logging.DbLog;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Service
public class LoggingServiceImpl implements LogService {

    private final DbLogRepository dbLogRepository;

    public LoggingServiceImpl(DbLogRepository dbLogRepository) {
        this.dbLogRepository = dbLogRepository;
    }

    @Override
    @Transactional
    public void log(String method,
                    String message,
                    String userId,
                    String ipAddress,
                    String menu,
                    String messageTemplate,
                    String logLevel,
                    String exception,
                    String properties) {

        DbLog log = new DbLog();
        log.setMethod(method);
        log.setLogMessage(message);
        log.setLogUserId(userId);
        log.setLogIpAddress(ipAddress != null ? ipAddress : resolveIp());
        log.setLogTime(LocalDateTime.now());
        log.setLogMenu(menu);
        log.setMessageTemplate(messageTemplate);
        log.setLogLevel(logLevel != null ? logLevel : logLevel);
        log.setException(exception);
        log.setProperties(properties);

        dbLogRepository.save(log);
        System.out.println("SAVE LOG SUCCESSFULLY");
    }

    @Override
    public void info(String method, String message, String userId, String menu, String messageTemplate) {
        log(method, message, userId, null, menu, messageTemplate, "INFO", null, null);
    }
    
    @Override
    public void error(String method, String message, String userId, String menu, String messageTemplate, String exception) {
        log(method, message, userId, null, menu, messageTemplate, "ERROR", exception, null);
    }
    
    @Override
    public void debug(String method, String message, String userId, String menu, String messageTemplate) {
        log(method, message, userId, null, menu, messageTemplate, "DEBUG", null, null);
    }

    // Resolve IP automatically
    private String resolveIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String ip = attrs.getRequest().getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = attrs.getRequest().getRemoteAddr();
            }
            return ip;
        }
        return getServerIp();
    }

    // Get server IP if not in web request
    private String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
