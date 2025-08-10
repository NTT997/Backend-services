package com.salesmanager.shop.application;

import java.util.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.config.ScheduledTask;



@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ShopApplication {

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(ShopApplication.class, args);
	}

}
