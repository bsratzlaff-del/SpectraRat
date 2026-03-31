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

        // 2. Gather all possible bands for THIS specific receiver
        List<FrequencyBand> allRxBands = new ArrayList<>();
        if (receiver.getFrequencyBand() != null) {
            allRxBands.add(receiver.getFrequencyBand());
        }
        if (receiver.getAvailableFrequencyBands() != null) {
            allRxBands.addAll(receiver.getAvailableFrequencyBands());
        }

        // 3. Get FCC Data
        List<FrequencyBand> inhibited = fccApiService.getInhibitedBandsByZip(zipCode);
        if (inhibited == null) inhibited = new ArrayList<>(); 


        // --- THE TRUTH LOGS ---
        System.out.println("DEBUG: Found " + inhibited.size() + " restricted bands for Zip " + zipCode);
        for (FrequencyBand b : inhibited) {
            System.out.println("    Restricted: " + b.getMinFrequency() + " - " + b.getMaxFrequency());
        }
        System.out.println("DEBUG: Testing Receiver with " + allRxBands.size() + " total hardware bands.");

        // 4. Format Compatible Capsules for the description
        String bundledMics = receiver.getCompatibleMicrophones().stream()
                .map(Microphone::getModelName)
                .collect(Collectors.joining(", "));
        String descriptionStr = "Compatible Capsules: " + (bundledMics.isEmpty() ? "None listed" : bundledMics);

        // 5. Spectral Density Ranking (Usable MHz / Total MHz)
        List<RecommendationResult> candidates = new ArrayList<>();

        for (FrequencyBand band : allRxBands) {
            double totalBandwidth = band.getMaxFrequency() - band.getMinFrequency();
            double blockedBandwidth = 0.0;

            for (FrequencyBand restricted : inhibited) {
                double overlapMin = Math.max(band.getMinFrequency(), restricted.getMinFrequency());
                double overlapMax = Math.min(band.getMaxFrequency(), restricted.getMaxFrequency());

                if (overlapMax > overlapMin) {
                    blockedBandwidth += (overlapMax - overlapMin);
                }
            }

            double usableMhz = totalBandwidth - blockedBandwidth;
            double score = (usableMhz / totalBandwidth) * 100.0;
            score = Math.round(score * 10.0) / 10.0;

            if (score < 50.0) score = 0.0; 

            String statusBadge;
            if (score >= 95) statusBadge = "EXCELLENT (Clear Spectrum)";
            else if (score >= 80) statusBadge = "GOOD (Minor Interference)";
            else if (score > 0) statusBadge = "POOR (Heavy TV Traffic)";
            else statusBadge = "RESTRICTED (Insufficient Spectrum)";

            String detailStr = String.format("%.1f MHz Usable | %s", usableMhz, descriptionStr);

            candidates.add(new RecommendationResult(
                    statusBadge,
                    score,
                    receiver.getManufacturer() + " " + receiver.getModelName() + " - Band " + band.getBandName(),
                    detailStr
            ));
        }

        // 6. Sort and Return (Limit commented out for Audit)
        List<RecommendationResult> results = candidates.stream()
                .sorted(Comparator.comparingDouble(RecommendationResult::getMatchPercentage).reversed())
                //.limit(3)
                .collect(Collectors.toList());

        System.out.println("Returning " + results.size() + " zip-ranked band recommendations.");
        return ResponseEntity.ok(results);
    }
}