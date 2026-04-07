package com.spectrarat.spectrarat.repository;

import com.spectrarat.spectrarat.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsernameIgnoreCase(String username);
    Optional<Customer> findByEmail(String email);
    
    // Add this exact line so the controller can find it
    Optional<Customer> findByUsername(String username); 
}