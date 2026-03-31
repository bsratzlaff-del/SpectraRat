package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.User; // Ensure you have a User model
import com.spectrarat.spectrarat.repository.UserRepository; // Ensure you have a UserRepository
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // LOGIN endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            // For Phase 3, you would typically generate and return a JWT token here.
            // For now, we return the user object to confirm successful authentication.
            return ResponseEntity.ok(userOptional.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password");
    }

    // LOGOUT endpoint (usually handled client-side by deleting the token)
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok("User logged out successfully");
    }
}