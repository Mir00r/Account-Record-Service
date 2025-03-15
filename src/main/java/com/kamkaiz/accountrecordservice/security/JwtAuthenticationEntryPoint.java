package com.kamkaiz.accountrecordservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements Spring Security's AuthenticationEntryPoint interface to handle authentication exceptions.
 * It serves as a central point for handling unauthorized access attempts to secured resources.
 *
 * Key responsibilities:
 * - Handles authentication errors when an unauthenticated user attempts to access protected resources
 * - Provides appropriate HTTP 401 (Unauthorized) response to the client
 * - Logs authentication failures for monitoring and debugging purposes
 *
 * This component is automatically triggered by Spring Security when an AuthenticationException occurs
 * during the authentication process.
 *
 * @see org.springframework.security.web.AuthenticationEntryPoint
 * @see org.springframework.security.core.AuthenticationException
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * This method is called by Spring Security when an AuthenticationException occurs during the authentication process.
     * It handles the authentication failure by sending an appropriate error response to the client.
     *
     * @param request The HttpServletRequest that resulted in an AuthenticationException
     * @param response The HttpServletResponse where the error will be written
     * @param authException The AuthenticationException that triggered this handler
     * @throws IOException If an input or output exception occurs
     * @throws ServletException If a servlet exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}