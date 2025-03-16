package com.kamkaiz.accountrecordservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Account Record Service application.
 * This Spring Boot application manages financial account transactions,
 * providing REST APIs for account management and transaction processing.
 * 
 * The application uses Spring Boot's auto-configuration capabilities
 * to set up the necessary components and dependencies.
 */
@SpringBootApplication
public class AccountRecordServiceApplication {

	/**
	 * Main method that starts the Spring Boot application.
	 * 
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(AccountRecordServiceApplication.class, args);
	}

}