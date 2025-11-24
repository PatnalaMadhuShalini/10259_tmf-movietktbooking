package com.sat.tmf.movietkt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                
            		 // Allow access to all files within WEB-INF folder (JSPs, etc)
            		 .requestMatchers("/WEB-INF/**").permitAll()	
            		// Public pages - anyone can access 
                .requestMatchers(
                    "/",          // Home Controller Mapping
                    "/home",      // Home Controller Mapping
                    "/login",     // Convenience redirect to custom login
                    "/register",  // Convenience redirect to custom register
                    "/user/login",      // UserController Login Mapping
                    "/user/register",   // UserController Register Mapping
                    "/movies",
                    "/movies/**", 
                    "/css/**", 
                    "/js/**", 
                    "/images/**",
                    "/static/**"
                ).permitAll()
                
                // Admin pages - require ADMIN role
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // All other pages require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/user/login") // <-- The URL for your GET /user/login controller
                .loginProcessingUrl("/login") // <-- The URL the form POSTs to
                .defaultSuccessUrl("/movies", true)
                .failureUrl("/user/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .authenticationProvider(authenticationProvider())
            .csrf(csrf -> csrf.disable());
            
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

