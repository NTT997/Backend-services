
package com.salesmanager.shop.application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.config.ScheduledTask;

import com.salesmanager.core.business.services.sql.SqlUtilities;
import com.salesmanager.core.business.services.log.LogService;
import org.codehaus.plexus.component.annotations.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ShopApplication implements ApplicationRunner{
	private static final Logger log = LogManager.getLogger(ShopApplication.class);
	
    private final LogService loggingService; // do NOT set to null

    // Spring will inject LoggingService automatically
    public ShopApplication(LogService loggingService) {
        this.loggingService = loggingService;
    }
	
	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(ShopApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	    //SqlUtilities.loggingERRORToLogDB("CLASSNAME","TEST LOG MESSAGE","LOG_USERID_FK","LOG_IP_ADDRESS","LogMenu","MESSAGE_TEMPLATE","EXCEPTION","PROPERTIES");
		// Info log - user viewed a menu
		loggingService.info(
		    ShopApplication.class.getName(), // method
		    "INIT DATABASE",        // message
		    "USR-001",                       // userId
		    "APPLICATION INIT MENU",                  // menu
		    "Fetching profile for user {0}"  // messageTemplate
		);



		SqlUtilities.printAllTables();
	}

}
