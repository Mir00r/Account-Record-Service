package com.kamkaiz.accountrecordservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * Aspect class that provides centralized logging functionality for the application.
 * This aspect intercepts method calls in the controller and service layers to log:
 * - Method entry with arguments
 * - Method exit with return values
 * - Execution time of methods
 * - Exception details if any occur
 * 
 * Uses Spring AOP to implement cross-cutting logging concerns without modifying business logic.
 * Generates and manages correlation IDs for request tracking across method calls.
 *
 * @see org.aspectj.lang.annotation.Aspect
 * @see org.springframework.stereotype.Component
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Advice that logs method entry and exit points for controller and service layer methods.
     * Implements around advice to capture both pre and post method execution details.
     *
     * @param joinPoint Provides reflective access to the advised method's details
     * @return The result from the advised method's execution
     * @throws Throwable If any error occurs during method execution
     */
    @Around("execution(* com.kamkaiz.accountrecordservice.controller..*(..)) || " +
            "execution(* com.kamkaiz.accountrecordservice.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        
        // Generate correlation ID for request tracking
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        
        try {
            // Log method entry
            logger.info("Entering method: {}() with arguments: {}",
                    signature.getMethod().getName(),
                    Arrays.toString(joinPoint.getArgs()));
            
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            // Log method exit
            logger.info("Exiting method: {}() with result: {}. Execution time: {} ms",
                    signature.getMethod().getName(),
                    result,
                    executionTime);
            
            return result;
        } catch (Exception e) {
            // Log exceptions
            logger.error("Exception in {}() with cause = {}",
                    signature.getMethod().getName(),
                    e.getMessage() != null ? e.getMessage() : "NULL",
                    e);
            throw e;
        } finally {
            MDC.remove("correlationId");
        }
    }
}