package com.kamkaiz.accountrecordservice.security;

import com.kamkaiz.accountrecordservice.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter handles JWT-based authentication for incoming HTTP requests.
 * It extends Spring Security's OncePerRequestFilter to ensure it is executed once per request.
 *
 * Key responsibilities:
 * - Extracts JWT token from the Authorization header
 * - Validates the JWT token using JwtTokenProvider
 * - Loads user details from UserDetailsService
 * - Sets up Spring Security context for authenticated users
 * - Handles authentication errors gracefully
 *
 * The filter processes the following steps:
 * 1. Extracts JWT from the Authorization header (Bearer token)
 * 2. Validates the token's signature and expiration
 * 3. Loads user details from the database
 * 4. Creates authentication token and sets security context
 *
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @see org.springframework.security.core.context.SecurityContextHolder
 * @see com.kamkaiz.accountrecordservice.service.UserDetailsServiceImpl
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Core filter method that processes each HTTP request for JWT authentication.
     * This method is called by the Spring Security filter chain for each request.
     *
     * @param request The HTTP request to process
     * @param response The HTTP response
     * @param filterChain The filter chain to continue processing
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * The token should be in the format: "Bearer <token>"
     *
     * @param request The HTTP request containing the JWT token
     * @return The JWT token string if present and properly formatted, null otherwise
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}