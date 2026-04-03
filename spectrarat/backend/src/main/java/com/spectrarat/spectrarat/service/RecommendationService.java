package com.spectrarat.spectrarat.service;

import com.spectrarat.spectrarat.dto.RecommendationResult;
import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.repository.ReceiverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final ReceiverRepository receiverRepo;
    private final FccApiService fccApiService;

    public RecommendationService(ReceiverRepository receiverRepo, FccApiService fccApiService) {
        this.receiverRepo = receiverRepo;
        this.fccApiService = fccApiService;
    }

    public List<RecommendationResult> generateRecommendations(String zipCode, Long receiverId) {
        log.debug("--- Starting Zip-Based Spectral Analysis for: {} ---", zipCode);

        Optional<Receiver> receiverOpt = receiverRepo.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            return new ArrayList<>();
        }
        Receiver receiver = receiverOpt.get();

        List<FrequencyBand> allRxBands = new ArrayList<>();
        if (receiver.getFrequencyBand() != null) {
            allRxBands.add(receiver.getFrequencyBand());
        }
        if (receiver.getAvailableFrequencyBands() != null) {
            allRxBands.addAll(receiver.getAvailableFrequencyBands());
        }

        List<FrequencyBand> inhibited = fccApiService.getInhibitedBandsByZip(zipCode);
        if (inhibited == null) {
            inhibited = new ArrayList<>();
        }

        log.debug("Found {} restricted bands for Zip {}", inhibited.size(), zipCode);
        inhibited.forEach(b -> log.debug("    Restricted: {} - {}", b.getMinFrequency(), b.getMaxFrequency()));
        log.debug("Testing Receiver with {} total hardware bands.", allRxBands.size());

        String bundledMics = receiver.getCompatibleMicrophones().stream()
                .map(Microphone::getModelName)
                .collect(Collectors.joining(", "));
        String descriptionStr = "Compatible Capsules: " + (bundledMics.isEmpty() ? "None listed" : bundledMics);

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
            double score = (totalBandwidth > 0) ? (usableMhz / totalBandwidth) * 100.0 : 0.0;
            score = Math.round(score * 10.0) / 10.0;

            if (score < 50.0) score = 0.0;

            String statusBadge = (score >= 95) ? "EXCELLENT (Clear Spectrum)"
                               : (score >= 80) ? "GOOD (Minor Interference)"
                               : (score > 0) ? "POOR (Heavy TV Traffic)"
                               : "RESTRICTED (Insufficient Spectrum)";

            String detailStr = String.format("%.1f MHz Usable | %s", usableMhz, descriptionStr);

            candidates.add(new RecommendationResult(statusBadge, score,
                    receiver.getManufacturer() + " " + receiver.getModelName() + " - Band " + band.getBandName(), detailStr));
        }

        return candidates.stream()
                .sorted(Comparator.comparingDouble(RecommendationResult::getMatchPercentage).reversed())
                .collect(Collectors.toList());
    }
}