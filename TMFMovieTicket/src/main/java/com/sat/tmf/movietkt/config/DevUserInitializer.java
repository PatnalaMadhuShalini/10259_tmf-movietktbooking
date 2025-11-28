package com.sat.tmf.movietkt.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sat.tmf.movietkt.entities.User;
import com.sat.tmf.movietkt.service.UserService;

@Configuration
public class DevUserInitializer {

    @Bean
    public CommandLineRunner createTestUser(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                if (userService.findByUsername("testuser") == null) {
                    User u = new User();
                    u.setUsername("testuser");
                    u.setPassword("Test@123");
                    u.setEmail("testuser@example.com");
                    u.setFullName("Test User");
                    u.setRole("USER");
                    userService.register(u);
                    System.out.println("Created test user 'testuser' with password 'Test@123'");
                } else {
                    System.out.println("Test user already exists");
                }
            } catch (Exception e) {
                System.err.println("Could not initialize test user: " + e.getMessage());
            }
        };
    }
}
