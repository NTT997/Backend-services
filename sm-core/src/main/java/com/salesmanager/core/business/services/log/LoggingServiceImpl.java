package com.salesmanager.core.business.services.log;

import com.salesmanager.core.business.repositories.log.DbLogRepository;
import com.salesmanager.core.model.logging.DbLog;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoggingServiceImpl implements LogService {

    private final DbLogRepository dbLogRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LoggingServiceImpl(DbLogRepository dbLogRepository) {
        this.dbLogRepository = dbLogRepository;
    }

    @Override
    @Transactional
    public void log(String method,
                    String message,
                    String ipAddress,
                    String menu,
                    String messageTemplate,
                    String logLevel,
                    String exception,
                    String properties) {

        DbLog log = new DbLog();
        log.setMethod(method);
        log.setLogMessage(message);
        log.setLogUserId(resolveUserId());
        log.setLogIpAddress(ipAddress != null ? ipAddress : resolveIp());
        log.setLogTime(LocalDateTime.now());
        log.setLogMenu(menu);
        log.setMessageTemplate(messageTemplate);
        log.setLogLevel(logLevel != null ? logLevel : logLevel);
        log.setException(exception);
        log.setProperties(properties);
        log.setRequest_url(resolveRequestURI());
        executor.submit(() -> {
            try {
                dbLogRepository.save(log);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        System.out.println("SAVE LOG SUCCESSFULLY");
    }

    @Override
    public void info(String method, String message, String menu) {
        log(method, message, null, menu, resolvePayload(), "INFO", null, null);
    }
    
    @Override
    public void error(String method, String message, String menu, String exception) {
        log(method, message, null, menu, resolvePayload(), "ERROR", exception, null);
    }
    
    @Override
    public void debug(String method, String message, String menu) {
        log(method, message, null, menu, resolvePayload(), "DEBUG", null, null);
    }


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

    private String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
    private String resolveRequestURI() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getRequest().getRequestURI();
        }
        return "unknown";
   }
    private String resolveUserId() {
        try {
            return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .map(auth -> auth.getName())
                    .orElse("anonymous");
        } catch (Exception e) {
            return "system";
        }
    }
    private String resolvePayload(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    try {
						return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
                }
            }
        }
        return null;
    }


}
