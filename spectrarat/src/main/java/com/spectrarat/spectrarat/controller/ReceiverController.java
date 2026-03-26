package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.repository.ReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receivers")
public class ReceiverController {

    @Autowired
    private ReceiverRepository receiverRepository;

    @GetMapping
    public List<Receiver> getAllReceivers() {
        return receiverRepository.findAll();
    }

    @PostMapping
    public Receiver createReceiver(@RequestBody Receiver receiver) {
        return receiverRepository.save(receiver);
    }
}
