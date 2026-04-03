package com.spectrarat.spectrarat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrarat.spectrarat.dto.RecommendationResult;
import com.spectrarat.spectrarat.service.RecommendationService;
import com.spectrarat.spectrarat.util.CsvUtil;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/top-candidates")
    public ResponseEntity<?> getTopThreeByLocation(
            @RequestParam String zipCode,
            @RequestParam Long receiverId) {
        List<RecommendationResult> results = recommendationService.generateRecommendations(zipCode, receiverId);
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
        List<RecommendationResult> results = recommendationService.generateRecommendations(zipCode, receiverId);

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
                    writer.printf("%s,%s,%.1f,%s,%s%n",
                            CsvUtil.escape(result.getModelName()),
                            CsvUtil.escape(result.getStatus()),
                            result.getMatchPercentage(),
                            CsvUtil.escape(result.getDescription()),
                            CsvUtil.escape(result.getTimestamp()));
                }
            }
        }
    }
}