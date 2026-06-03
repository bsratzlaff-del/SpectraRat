package com.spectrarat.spectrarat.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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


@RestController
@RequestMapping("/api/frequency-bands")
// NOTE: Allowing all origins for development. For production, you should restrict this.
@CrossOrigin(origins = "*")
public class FrequencyBandController {

    private final JpaRepository<Object, Long> frequencyBandRepository;

    public FrequencyBandController(JpaRepository<Object, Long> frequencyBandRepository) {
        this.frequencyBandRepository = frequencyBandRepository;
    }

    // This maps to: GET /api/frequency-bands
    @GetMapping
    public List<Object> getAllFrequencyBands() {
        return frequencyBandRepository.findAll();
    }

    @GetMapping("/validate")
    public String validateApiConnection() {
        return "Connection successful!";
    }

    // Fixed: Removed redundant "/frequency-bands" from path
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFrequencyBandById(@PathVariable Long id) {
        return frequencyBandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Fixed: Removed redundant "/frequency-bands" from path
    @PostMapping 
    public ResponseEntity<Object> createFrequencyBand(@RequestBody Object frequencyBand) {
        Object savedBand = frequencyBandRepository.save(frequencyBand);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(savedBand);
    }

    // Fixed: Removed redundant "/frequency-bands" from path
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFrequencyBand(@PathVariable Long id, @RequestBody Object bandDetails) {
        return frequencyBandRepository.findById(id)
                .map(existingBand -> ResponseEntity.ok(frequencyBandRepository.save(bandDetails)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Fixed: Removed redundant "/frequency-bands" from path
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