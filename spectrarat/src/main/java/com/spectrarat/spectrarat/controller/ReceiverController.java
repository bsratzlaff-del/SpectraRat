package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.repository.ReceiverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/receivers")
public class ReceiverController {

    private final ReceiverRepository receiverRepository;

    public ReceiverController(ReceiverRepository receiverRepository) {
        this.receiverRepository = receiverRepository;
    }

    // GET all receivers for the marketplace list
    @GetMapping
    public List<Receiver> getAllReceivers() {
        return receiverRepository.findAll();
    }

    // GET receiver by ID for detailed spectral specs
    @GetMapping("/{id}")
    public ResponseEntity<Receiver> getReceiverById(@PathVariable Long id) {
        return receiverRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST a new receiver (Returns 201 Created)
    @PostMapping
    public ResponseEntity<Receiver> createReceiver(@RequestBody Receiver receiver) {
        Receiver savedReceiver = receiverRepository.save(receiver);
        return new ResponseEntity<>(savedReceiver, HttpStatus.CREATED);
    }

    // PUT to update receiver details (Brand, Model, etc.)
    @PutMapping("/{id}")
    public ResponseEntity<Receiver> updateReceiver(@PathVariable Long id, @RequestBody Receiver receiverDetails) {
        return receiverRepository.findById(id)
                .map(existingReceiver -> {
                    existingReceiver.setManufacturer(receiverDetails.getManufacturer());
                    existingReceiver.setModelName(receiverDetails.getModelName());
                    // Update other specific hardware fields
                    return ResponseEntity.ok(receiverRepository.save(existingReceiver));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a receiver from the inventory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceiver(@PathVariable Long id) {
        return receiverRepository.findById(id)
                .map(receiver -> {
                    receiverRepository.delete(receiver);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}