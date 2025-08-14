package com.salesmanager.core.business.services.log;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.salesmanager.core.model.log.AppLog;



public interface LogService {
	
	public void log(String method,
            String message,
            String userId,
            String ipAddress,
            String menu,
            String messageTemplate,
            String logLevel,
            String exception,
            String properties);
	
	public void info(String method, String message, String userId, String menu, String messageTemplate);
	
	public void error(String method, String message, String userId, String menu, String messageTemplate, String exception);
	
	public void debug(String method, String message, String userId, String menu, String messageTemplate);
	
}
