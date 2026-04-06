package com.spectrarat.spectrarat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/frequency-bands") // BASE PATH
public class FrequencyBandController {

    private final FrequencyBandRepository frequencyBandRepository;

    public FrequencyBandController(FrequencyBandRepository frequencyBandRepository) {
        this.frequencyBandRepository = frequencyBandRepository;
    }

    
    @GetMapping
    public List<FrequencyBand> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    @GetMapping("/validate")
    public String validateApiConnection() {
        return "Connection successful!";
    }

    @GetMapping("/frequency-bands/{id}")
    public ResponseEntity<FrequencyBand> getFrequencyBandById(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/frequency-bands") // Changed from /bands
    public ResponseEntity<FrequencyBand> createFrequencyBand(@RequestBody FrequencyBand frequencyBand) {
        FrequencyBand savedBand = frequencyBandRepository.save(frequencyBand);
        return new ResponseEntity<>(savedBand, HttpStatus.CREATED);
    }

    @PutMapping("/frequency-bands/{id}")
    public ResponseEntity<FrequencyBand> updateFrequencyBand(@PathVariable Long id, @RequestBody FrequencyBand bandDetails) {
        return frequencyBandRepository.findById(id)
                .map(existingBand -> {
                    existingBand.setBandName(bandDetails.getBandName());
                    existingBand.setMinFrequency(bandDetails.getMinFrequency());
                    existingBand.setMaxFrequency(bandDetails.getMaxFrequency());
                    return ResponseEntity.ok(frequencyBandRepository.save(existingBand));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/frequency-bands/{id}")
    public ResponseEntity<Void> deleteFrequencyBand(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(band -> {
                    frequencyBandRepository.delete(band);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}