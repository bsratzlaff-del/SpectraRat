package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/frequency-bands")
public class FrequencyBandController {

    private final FrequencyBandRepository frequencyBandRepository;

    public FrequencyBandController(FrequencyBandRepository frequencyBandRepository) {
        this.frequencyBandRepository = frequencyBandRepository;
    }
    


    // Handles GET requests to /api/frequency-bands
    @GetMapping
    public List<FrequencyBand> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    // Handles POST requests to /api/frequency-bands
    @PostMapping
    public ResponseEntity<FrequencyBand> createFrequencyBand(@RequestBody FrequencyBand frequencyBand) {
        
        FrequencyBand savedBand = frequencyBandRepository.save(frequencyBand);
        return new ResponseEntity<>(savedBand, HttpStatus.CREATED); 
    }
}