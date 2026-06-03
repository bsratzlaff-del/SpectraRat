package com.spectrarat.spectrarat.controller;

import java.net.URI;
import java.util.List;

// Make sure your IDE imports your specific entity and repository here!

import com.spectrarat.spectrarat.repository.FrequencyBandRepository;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spectrarat.spectrarat.model.FrequencyBand;

@RestController
@RequestMapping("/api/frequency-bands")
// NOTE: Allowing all origins for development. For production, you should restrict this.
@CrossOrigin(origins = "*")
public class FrequencyBandController {

    // 1. Changed generic JpaRepository to your specific FrequencyBandRepository
    private final FrequencyBandRepository frequencyBandRepository;

    public FrequencyBandController(FrequencyBandRepository frequencyBandRepository) {
        this.frequencyBandRepository = frequencyBandRepository;
    }

    // 2. Changed Object to FrequencyBand
    @GetMapping
    public List<FrequencyBand> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    @GetMapping("/validate")
    public String validateApiConnection() {
        return "Connection successful!";
    }

    // 3. Changed Object to FrequencyBand
    @GetMapping("/{id}")
    public ResponseEntity<FrequencyBand> getFrequencyBandById(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Changed Object to FrequencyBand
    @PostMapping 
    public ResponseEntity<FrequencyBand> createFrequencyBand(@RequestBody FrequencyBand frequencyBand) {
        FrequencyBand savedBand = frequencyBandRepository.save(frequencyBand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(savedBand);
    }

    // 5. Changed Object to FrequencyBand
    @PutMapping("/{id}")
    public ResponseEntity<FrequencyBand> updateFrequencyBand(@PathVariable Long id, @RequestBody FrequencyBand bandDetails) {
        return frequencyBandRepository.findById(id)
                .map(existingBand -> {
                    // Update the existing band's properties here if needed, then save
                    return ResponseEntity.ok(frequencyBandRepository.save(bandDetails));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrequencyBand(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(band -> {
                    frequencyBandRepository.delete(band);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}