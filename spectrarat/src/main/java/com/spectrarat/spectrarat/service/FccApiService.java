package com.spectrarat.spectrarat.service;

import com.spectrarat.spectrarat.model.FrequencyBand;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
            new FrequencyBand("VLF", 3, 30), // Very Low Frequency
            new FrequencyBand("LF", 30, 300), // Low Frequency
            new FrequencyBand("MF", 300, 3000), // Medium Frequency
            new FrequencyBand("HF", 3000, 30000), // High Frequency
            new FrequencyBand("VHF", 30000, 300000), // Very High Frequency
            new FrequencyBand("UHF", 300000, 3000000), // Ultra High Frequency
            new FrequencyBand("SHF", 3000000, 30000000), // Super High Frequency
            new FrequencyBand("EHF", 30000000, 300000000) // Extremely High Frequency
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
}