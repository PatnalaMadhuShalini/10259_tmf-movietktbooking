package com.sat.tmf.movietkt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sat.tmf.movietkt.entities.User;
import com.sat.tmf.movietkt.service.UserService;

import java.util.Collections;

/**
 * Custom UserDetailsService Implementation
 * 
 * This service integrates our custom User entity with Spring Security.
 * It loads user details from the database during authentication.
 * 
 * WORKFLOW:
 * 1. User enters username/password on login form
 * 2. Spring Security calls loadUserByUsername()
 * 3. We fetch user from database via UserService
 * 4. Convert our User entity to Spring Security's UserDetails
 * 5. Spring Security compares passwords and grants authentication
 * 
 * ROLES:
 * - USER: Regular customers who can browse and book tickets
 * - ADMIN: Administrators who can manage movies, screens, and shows
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database
        User user = userService.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Convert our User entity to Spring Security UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Already BCrypt encoded in database
                .authorities(Collections.singletonList(
                    // Add "ROLE_" prefix as required by Spring Security
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
                ))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
