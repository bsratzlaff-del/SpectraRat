package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Customer;
import com.spectrarat.spectrarat.model.User;
import com.spectrarat.spectrarat.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REGISTER endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Customer customer) {
        // Simple check for username
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Username is already taken!");
        }
        
        // Encode and save
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setPassword(null);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    // LOGIN endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) { 
        // We look for the customer by the username provided in the login request
        Optional<Customer> customerOpt = customerRepository.findByUsername(loginRequest.getUsername());

        if (customerOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), customerOpt.get().getPassword())) {
            Customer customer = customerOpt.get();
            customer.setPassword(null); 
            return ResponseEntity.ok(customer);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok().body("User logged out successfully");
    }
}