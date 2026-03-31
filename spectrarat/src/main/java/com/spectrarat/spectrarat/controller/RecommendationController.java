package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.dto.RecommendationResult;
import com.spectrarat.spectrarat.model.*;
import com.spectrarat.spectrarat.repository.*;
import com.spectrarat.spectrarat.service.FccApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        List<RecommendationResult> results = generateRecommendations(zipCode, receiverId);
        if (results.isEmpty()) {
            // Assuming generateRecommendations returns an empty list if receiver is not found.
            return ResponseEntity.status(404).body("Receiver not found or no recommendations available.");
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/report")
    public void downloadReport(
            @RequestParam String zipCode,
            @RequestParam Long receiverId,
            HttpServletResponse response) throws IOException {

        // 1. Generate the data
        List<RecommendationResult> results = generateRecommendations(zipCode, receiverId);

        // 2. Set up HTTP response for CSV download
        response.setContentType("text/csv");
        String reportTitle = "Recommendation_Report_Zip_" + zipCode + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + reportTitle + "\"");

        // 3. Write CSV content
        try (PrintWriter writer = response.getWriter()) {
            // Report Title
            writer.println("Recommendation Report for Zip Code: " + zipCode);
            writer.println("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(); // Blank line

            // CSV Header (Multiple Columns)
            writer.println("Model Name,Status,Match Percentage,Description,Timestamp");

            // CSV Rows (Multiple Rows)
            if (results.isEmpty()) {
                writer.println("No recommendations found for the given criteria.");
            } else {
                for (RecommendationResult result : results) {
                    writer.printf("\"%s\",\"%s\",%.1f,\"%s\",\"%s\"%n",
                            escapeCsv(result.getModelName()),
                            escapeCsv(result.getStatus()),
                            result.getMatchPercentage(),
                            escapeCsv(result.getDescription()),
                            escapeCsv(result.getTimestamp()));
                }
            }
        }
    }

    /**
     * Helper to escape characters for CSV format.
     */
    private String escapeCsv(String data) {
        if (data == null) {
            return "";
        }
        // If the data contains a comma, quote, or newline, wrap it in double quotes.
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            // Escape existing double quotes by doubling them up.
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }

    private List<RecommendationResult> generateRecommendations(String zipCode, Long receiverId) {

        System.out.println("--- Starting Zip-Based Spectral Analysis for: " + zipCode + " ---");

        // 1. Find Receiver
        Optional<Receiver> receiverOpt = receiverRepo.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            return new ArrayList<>();
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

        // 6. Sort and Return
        List<RecommendationResult> results = candidates.stream()
                .sorted(Comparator.comparingDouble(RecommendationResult::getMatchPercentage).reversed())
                .collect(Collectors.toList());

        System.out.println("Returning " + results.size() + " zip-ranked band recommendations.");
        return results;
    }
}