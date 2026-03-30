package com.spectrarat.spectrarat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrarat.spectrarat.model.Customer;
import com.spectrarat.spectrarat.repository.CustomerRepository;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        // Check if username or email already exists
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken!");
        }

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }

        // Encode the password before saving
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully!");
    }
}