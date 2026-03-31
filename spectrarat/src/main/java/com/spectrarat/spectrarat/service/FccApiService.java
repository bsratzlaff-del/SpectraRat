package com.spectrarat.spectrarat.service;

import com.spectrarat.spectrarat.model.FrequencyBand;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FccApiService {

    private final WebClient webClient;

    public FccApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://opendata.fcc.gov").build();
    }

    // Method to identify FCC spectrum bands (static list based on frequency allocation table)
    public List<FrequencyBand> getFccSpectrumBands() {
        return Arrays.asList(
            new FrequencyBand(null, "VLF", 3.0, 30.0), // Very Low Frequency
            new FrequencyBand(null, "LF", 30.0, 300.0), // Low Frequency
            new FrequencyBand(null, "MF", 300.0, 3000.0), // Medium Frequency
            new FrequencyBand(null, "HF", 3000.0, 30000.0), // High Frequency
            new FrequencyBand(null, "VHF", 30000.0, 300000.0), // Very High Frequency
            new FrequencyBand(null, "UHF", 300000.0, 3000000.0), // Ultra High Frequency
            new FrequencyBand(null, "SHF", 3000000.0, 30000000.0), // Super High Frequency
            new FrequencyBand(null, "EHF", 30000000.0, 300000000.0) // Extremely High Frequency
        );
    }

    // Method to validate connection to FCC Public Data API
    public Mono<String> validateApiConnection() {
        return webClient.get()
                .uri("/resource/acbv-jbb4.json?$limit=1")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> "API Connection Successful: " + response)
                .onErrorResume(e -> Mono.just("API Connection Failed: " + e.getMessage()));
    }
    public List<FrequencyBand> getInhibitedBandsByZip(String zipCode) {
        List<FrequencyBand> restricted = new ArrayList<>();
        
        // TODO: Future enhancement - query FCC database by zip code for active TV channels.
        // For the capstone, we mock this logic to demonstrate the algorithm works.
        
        if ("90210".equals(zipCode)) {
            // Mocking a restricted 600MHz Guard Band (Assuming your units are in MHz for mics)
            restricted.add(new FrequencyBand(null, "Restricted 600MHz Guard Band", 600.0, 608.0)); 
        } else if ("10001".equals(zipCode)) {
            // Mocking heavy NYC DTV interference in the 500MHz range
            restricted.add(new FrequencyBand(null, "NYC DTV Channel 22-24", 518.0, 536.0));
        }
        
        return restricted;
    }
}