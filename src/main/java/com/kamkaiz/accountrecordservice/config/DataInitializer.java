package com.kamkaiz.accountrecordservice.config;

import com.kamkaiz.accountrecordservice.model.User;
import com.kamkaiz.accountrecordservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Component responsible for initializing default admin user data. This runs when the application
 * starts and ensures an admin user exists.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
  private static final String ADMIN_USERNAME = "admin";

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
      User adminUser = User.builder()
        .username(ADMIN_USERNAME)
        .email("admin@admin.com")
        .enabled(true)
        .password(passwordEncoder.encode("password"))
        .roles(Collections.singleton("ROLE_ADMIN"))
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

      userRepository.save(adminUser);
      logger.info("Default admin user created successfully");
    } else {
      logger.info("Admin user already exists");
    }
  }
}
