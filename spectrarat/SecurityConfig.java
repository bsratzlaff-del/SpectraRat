package com.spectrarat.spectrarat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API development (consider enabling for production)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").permitAll() // Allow all requests to /api/** for now
                .anyRequest().authenticated() // All other requests require authentication
            );
        return http.build();
    }

    /**
     * Defines a PasswordEncoder bean for hashing passwords.
     * BCryptPasswordEncoder is a strong, industry-standard hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}