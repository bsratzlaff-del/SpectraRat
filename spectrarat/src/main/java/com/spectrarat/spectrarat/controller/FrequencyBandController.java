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

    @GetMapping
    public List<FrequencyBand> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    // Added to help the frontend find bands for a specific hardware item
    @GetMapping("/{id}")
    public ResponseEntity<FrequencyBand> getFrequencyBandById(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FrequencyBand> createFrequencyBand(@RequestBody FrequencyBand frequencyBand) {
        // If you hit a Foreign Key constraint here, ensure the 'receiver' or 'microphone' 
        // object inside the JSON payload has a valid ID.
        FrequencyBand savedBand = frequencyBandRepository.save(frequencyBand);
        return new ResponseEntity<>(savedBand, HttpStatus.CREATED);
    }

    // Patch: Adding Update capability for Phase 3
    @PutMapping("/{id}")
    public ResponseEntity<FrequencyBand> updateFrequencyBand(@PathVariable Long id, @RequestBody FrequencyBand bandDetails) {
        return frequencyBandRepository.findById(id)
                .map(existingBand -> {
                    existingBand.setBandName(bandDetails.getBandName());
                    existingBand.setMinFrequency(bandDetails.getMinFrequency());
                    existingBand.setMaxFrequency(bandDetails.getMaxFrequency());
                    // Update any hardware associations here if necessary
                    return ResponseEntity.ok(frequencyBandRepository.save(existingBand));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Patch: Adding Delete for marketplace cleanup
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrequencyBand(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(band -> {
                    frequencyBandRepository.delete(band);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}