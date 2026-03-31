package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.dto.RecommendationResult;
import com.spectrarat.spectrarat.model.*;
import com.spectrarat.spectrarat.repository.*;
import com.spectrarat.spectrarat.service.FccApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final MicrophoneRepository micRepo;
    private final ReceiverRepository receiverRepo;
    private final FccApiService fccApiService;

    public RecommendationController(MicrophoneRepository micRepo, ReceiverRepository receiverRepo, FccApiService fccApiService) {
        this.micRepo = micRepo;
        this.receiverRepo = receiverRepo;
        this.fccApiService = fccApiService;
    }

    @GetMapping("/top-candidates")
    public ResponseEntity<?> getTopThreeByLocation(
            @RequestParam String zipCode,
            @RequestParam Long receiverId) {

        System.out.println("--- Starting Zip-Based Spectral Analysis for: " + zipCode + " ---");

        // 1. Find Receiver
        Optional<Receiver> receiverOpt = receiverRepo.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Receiver not found.");
        }
        Receiver receiver = receiverOpt.get();

        // 2. Get FCC Data
        List<FrequencyBand> inhibited = fccApiService.getInhibitedBandsByZip(zipCode);
        if (inhibited == null) inhibited = new ArrayList<>();

        // 3. Gather all possible bands for THIS specific receiver
        List<FrequencyBand> allRxBands = new ArrayList<>();
        if (receiver.getFrequencyBand() != null) {
            allRxBands.add(receiver.getFrequencyBand());
        }
        if (receiver.getAvailableFrequencyBands() != null) {
            allRxBands.addAll(receiver.getAvailableFrequencyBands());
        }

        // 4. Format Compatible Capsules for the description
        String bundledMics = receiver.getCompatibleMicrophones().stream()
                .map(Microphone::getModelName)
                .collect(Collectors.joining(", "));
        String descriptionStr = "Compatible Capsules: " + (bundledMics.isEmpty() ? "None listed" : bundledMics);

        // 5. Score and Rank each Frequency Band based on Zip Code RF Proximity
        List<RecommendationResult> candidates = new ArrayList<>();

        for (FrequencyBand band : allRxBands) {
            double score = 100.0;
            double nearestInterference = Double.MAX_VALUE;
            
            for (FrequencyBand restricted : inhibited) {
                // ZERO TOLERANCE: If it overlaps OR TOUCHES an FCC restriction boundary, it's 0.0 instantly.
                if (band.getMinFrequency() <= restricted.getMaxFrequency() && band.getMaxFrequency() >= restricted.getMinFrequency()) {
                    score = 0.0; 
                    break;
                }
                
                // PROXIMITY RANKING: Calculate distance to the nearest illegal station
                double distToBottomEdge = Math.abs(band.getMinFrequency() - restricted.getMaxFrequency());
                double distToTopEdge = Math.abs(band.getMaxFrequency() - restricted.getMinFrequency());
                double closestEdge = Math.min(distToBottomEdge, distToTopEdge);
                
                if (closestEdge < nearestInterference) {
                    nearestInterference = closestEdge;
                }
            }

            // Apply a "Safety Buffer" penalty to create a true ranking among legal bands
            if (score > 0 && nearestInterference != Double.MAX_VALUE) {
                // If it's within 20MHz of interference, dock points proportional to closeness.
                double proximityPenalty = Math.max(0, 15.0 - nearestInterference);
                score -= proximityPenalty;
                
                // Round to 1 decimal place for clean JSON (e.g., 98.4)
                score = Math.round(score * 10.0) / 10.0;
            }

            // Assign status based on the final zip-based score
            String statusBadge;
            if (score >= 90) statusBadge = "EXCELLENT MATCH";
            else if (score > 0) statusBadge = "GOOD MATCH";
            else statusBadge = "RESTRICTED (FCC OVERLAP)";

            candidates.add(new RecommendationResult(
                    statusBadge,
                    score,
                    receiver.getManufacturer() + " " + receiver.getModelName() + " - Band " + band.getBandName(),
                    descriptionStr
            ));
        }

        // 6. Sort by highest score (Zip-based ranking) and limit to top 3
        List<RecommendationResult> top3 = candidates.stream()
                .sorted(Comparator.comparingDouble(RecommendationResult::getMatchPercentage).reversed())
                .limit(3)
                .collect(Collectors.toList());

        System.out.println("Returning " + top3.size() + " zip-ranked band recommendations.");
        return ResponseEntity.ok(top3);
    }
}