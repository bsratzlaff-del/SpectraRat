package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/systems")
    public ResponseEntity<List<Receiver>> recommendSystem(@RequestParam double budget) {
        if (budget <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Receiver> recommendations = recommendationService.recommendSystem(budget);
        return ResponseEntity.ok(recommendations);
    }
}