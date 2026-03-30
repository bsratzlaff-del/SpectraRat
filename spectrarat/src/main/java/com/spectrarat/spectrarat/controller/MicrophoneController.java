package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/microphones")
public class MicrophoneController {

    private final MicrophoneRepository microphoneRepository;

    public MicrophoneController(MicrophoneRepository microphoneRepository) {
        this.microphoneRepository = microphoneRepository;
    }

    @PostMapping
    public ResponseEntity<Microphone> createMicrophone(@RequestBody Microphone microphone) {
       
        Microphone savedMicrophone = microphoneRepository.save(microphone);

        return new ResponseEntity<>(savedMicrophone, HttpStatus.CREATED);
        
    }
    // Handles GET requests to /api/microphones
    @GetMapping
    public List<Microphone> getAllMicrophones() {
        return microphoneRepository.findAll();
    }
}