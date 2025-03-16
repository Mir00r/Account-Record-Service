package com.kamkaiz.accountrecordservice.config;

import com.kamkaiz.accountrecordservice.model.Account;
import com.kamkaiz.accountrecordservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Component responsible for initializing account data from accounts.txt file.
 * This runs when the application starts and ensures account records are loaded.
 */
@Component
@RequiredArgsConstructor
public class AccountDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AccountDataInitializer.class);
    private static final String ACCOUNTS_FILE = "accounts.txt";

    private final JobLauncher jobLauncher;
    private final Job importAccountsJob;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        try {
            // Check if accounts.txt exists
            ClassPathResource resource = new ClassPathResource(ACCOUNTS_FILE);
            if (!resource.exists()) {
                logger.warn("accounts.txt file not found in classpath");
                return;
            }

            // Check if any accounts exist in the database
            if (accountRepository.count() > 0) {
                logger.info("Account records already exist in the database");
                return;
            }

            // Check if the file has content (more than just header)
            boolean hasContent = false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                // Skip header
                reader.readLine();
                hasContent = reader.readLine() != null;
            }

            if (!hasContent) {
                logger.warn("accounts.txt file is empty or contains only header");
                return;
            }

            // Run the batch job to import accounts
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(importAccountsJob, jobParameters);
            logger.info("Account records imported successfully");

        } catch (Exception e) {
            logger.error("Error initializing account data", e);
        }
    }
}