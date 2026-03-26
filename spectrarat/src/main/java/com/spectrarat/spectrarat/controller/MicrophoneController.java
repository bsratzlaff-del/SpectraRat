package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/microphones")
public class MicrophoneController {

    @Autowired
    private MicrophoneRepository microphoneRepository;

    // Handles GET requests to /api/microphones
    @GetMapping
    public List<Microphone> getAllMicrophones() {
        return microphoneRepository.findAll();
    }

    // Handles POST requests to /api/microphones
    @PostMapping
    public Microphone createMicrophone(@RequestBody Microphone microphone) {
        return microphoneRepository.save(microphone);
    }
}