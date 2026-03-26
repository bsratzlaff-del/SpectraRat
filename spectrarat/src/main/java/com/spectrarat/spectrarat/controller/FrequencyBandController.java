package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frequency-bands")
public class FrequencyBandController {

    @Autowired
    private FrequencyBandRepository frequencyBandRepository;

    // Handles GET requests to /api/frequency-bands
    @GetMapping
    public List<FrequencyBand> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    // Handles POST requests to /api/frequency-bands
    @PostMapping
    public FrequencyBand createFrequencyBand(@RequestBody FrequencyBand frequencyBand) {
        return frequencyBandRepository.save(frequencyBand);
    }
}