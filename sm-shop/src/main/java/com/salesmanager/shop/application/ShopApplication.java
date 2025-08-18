
package com.salesmanager.shop.application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import com.salesmanager.core.business.services.log.LogService;
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
		    "APPLICATION INIT MENU" // menu
		);
	}

}
