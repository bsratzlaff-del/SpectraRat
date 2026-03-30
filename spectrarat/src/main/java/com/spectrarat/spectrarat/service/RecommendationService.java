package com.spectrarat.spectrarat.service;

import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import com.spectrarat.spectrarat.repository.ReceiverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final MicrophoneRepository microphoneRepository;
    private final ReceiverRepository receiverRepository;

    public RecommendationService(MicrophoneRepository microphoneRepository, ReceiverRepository receiverRepository) {
        this.microphoneRepository = microphoneRepository;
        this.receiverRepository = receiverRepository;
    }

    /**
     * Finds receivers that have compatible microphones fitting within a given budget.
     * This is a simple example of a recommendation algorithm.
     * @param budget The maximum total cost.
     * @return A list of receivers that have at least one compatible microphone within budget.
     */
    public List<Receiver> recommendSystem(double budget) {
        List<Receiver> allReceivers = receiverRepository.findAll();

        // This logic finds receivers that have at least one compatible microphone
        // where the combined cost is within the budget.
        return allReceivers.stream()
                .filter(receiver -> receiver.getCompatibleMicrophones().stream()
                        .anyMatch(mic -> receiver.calculateBundleCost(mic) <= budget))
                .collect(Collectors.toList());
    }
}