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

@Aspect
@Component
public class LoggingAspect {

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