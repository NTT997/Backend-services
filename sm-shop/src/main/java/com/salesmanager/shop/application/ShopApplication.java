package com.salesmanager.shop.application;

import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.config.ScheduledTask;

import com.salesmanager.core.business.services.sql.SqlUtilities;

import org.codehaus.plexus.component.annotations.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class ShopApplication{

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(ShopApplication.class, args);
	}


}
