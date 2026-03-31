package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Patch: Enables Angular access
@RestController
@RequestMapping("/api/microphones")
public class MicrophoneController {

    private final MicrophoneRepository microphoneRepository;

    public MicrophoneController(MicrophoneRepository microphoneRepository) {
        this.microphoneRepository = microphoneRepository;
    }

    @GetMapping
    public List<Microphone> getAllMicrophones() {
        return microphoneRepository.findAll();
    }

    // Patch: Added for individual marketplace item detail views
    @GetMapping("/{id}")
    public ResponseEntity<Microphone> getMicrophoneById(@PathVariable Long id) {
        return microphoneRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Microphone> createMicrophone(@RequestBody Microphone microphone) {
        Microphone savedMicrophone = microphoneRepository.save(microphone);
        return new ResponseEntity<>(savedMicrophone, HttpStatus.CREATED);
    }

    // Patch: Update functionality for inventory management
    @PutMapping("/{id}")
    public ResponseEntity<Microphone> updateMicrophone(@PathVariable Long id, @RequestBody Microphone micDetails) {
        return microphoneRepository.findById(id)
                .map(existingMic -> {
                    existingMic.setManufacturer(micDetails.getManufacturer());
                    existingMic.setModelName(micDetails.getModelName());
                    existingMic.setCost(micDetails.getCost());
                    existingMic.setFrequencyBand(micDetails.getFrequencyBand());
                    existingMic.setCapsuleType(micDetails.getCapsuleType());
                    // Add any other specific fields like 'type' or 'frequencyRange'
                    return ResponseEntity.ok(microphoneRepository.save(existingMic));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Patch: Delete functionality for marketplace cleanup
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMicrophone(@PathVariable Long id) {
        return microphoneRepository.findById(id)
                .map(mic -> {
                    microphoneRepository.delete(mic);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}