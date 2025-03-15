package com.kamkaiz.accountrecordservice.service;

import com.kamkaiz.accountrecordservice.model.User;
import com.kamkaiz.accountrecordservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of Spring Security's UserDetailsService for custom user authentication.
 * This service provides the core user loading functionality required by Spring Security
 * to perform authentication and authorization.
 *
 * Key responsibilities:
 * - Loads user details from the database during authentication
 * - Converts domain User entities to Spring Security's UserDetails
 * - Maps user roles to Spring Security authorities
 * - Handles user not found scenarios
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see com.kamkaiz.accountrecordservice.model.User
 * @see org.springframework.security.core.userdetails.UserDetails
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by username for authentication purposes.
     * This method is called by Spring Security during the authentication process.
     * It retrieves the user from the database and converts it to Spring Security's UserDetails format.
     *
     * @param username The username to search for
     * @return UserDetails object containing the user's authentication and authorization information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }
}