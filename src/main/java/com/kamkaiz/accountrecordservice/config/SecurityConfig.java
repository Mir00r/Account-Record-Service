package com.kamkaiz.accountrecordservice.config;

import com.kamkaiz.accountrecordservice.security.JwtAuthenticationEntryPoint;
import com.kamkaiz.accountrecordservice.security.JwtAuthenticationFilter;
import com.kamkaiz.accountrecordservice.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security settings in the application.
 * This class sets up security configurations including JWT authentication,
 * password encoding, and URL-based security rules.
 *
 * Key responsibilities:
 * - Configures authentication manager with custom UserDetailsService
 * - Sets up JWT-based authentication filter
 * - Defines security rules for different URL patterns
 * - Configures password encoding
 * - Manages CORS and CSRF settings
 * - Handles unauthorized access through custom entry point
 *
 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
 * @see com.kamkaiz.accountrecordservice.security.JwtAuthenticationFilter
 * @see com.kamkaiz.accountrecordservice.service.UserDetailsServiceImpl
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configures the authentication manager with custom UserDetailsService and password encoder.
     * This setup is crucial for the authentication process.
     *
     * @param authenticationManagerBuilder The builder for authentication configuration
     * @throws Exception if there's an error in configuration
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Exposes the AuthenticationManager as a Bean to be used in other components.
     * This is required for JWT authentication.
     *
     * @return The AuthenticationManager instance
     * @throws Exception if there's an error creating the bean
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Creates a PasswordEncoder bean for secure password hashing.
     * Uses BCrypt hashing algorithm for password encoding.
     *
     * @return The configured PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security settings for HTTP requests.
     * This method sets up:
     * - CORS and CSRF settings
     * - Session management (stateless)
     * - URL-based security rules
     * - JWT filter integration
     * - H2 console access (for development)
     *
     * @param http The HttpSecurity to modify
     * @throws Exception if there's an error in configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // For H2 Console
        http.headers().frameOptions().disable();
    }
}