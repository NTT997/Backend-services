package com.salesmanager.shop.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableMongoRepositories
public class ShopApplication implements CommandLineRunner{

	public static void main(String[] args) {

		SpringApplication.run(ShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// MongoDB connection URI
		String uri = ""; // Replace with your MongoDB URI
		System.out.println("===========================RUNNING CONNECTION===========================");
		try {		
			MongoClient mongoClient = MongoClients.create(
				MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(uri))
				.build());
			System.out.println("===========================RUNNING CONNECTION LINE 34===========================");
			MongoDatabase database = mongoClient.getDatabase("test"); // Replace "test" with your database name
			System.out.println("===========================RUNNING CONNECTION LINE 36===========================");
			System.out.println("Connected to MongoDB. Collections in the database:");
			database.listCollectionNames().forEach(System.out::println);
		} catch (Exception e) {
			System.err.println("Failed to connect to MongoDB: " + e.getMessage());
			throw e; 
		}
		System.out.println("===========================RUNNING SUCCESS===========================");
	}


}
