package com.spectrarat.spectrarat.controller;

import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.service.FccApiService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Patch: Enables Angular access to FCC data
@RestController
@RequestMapping("/api/fcc")
public class FccController {

    private final FccApiService fccApiService;

    public FccController(FccApiService fccApiService) {
        this.fccApiService = fccApiService;
    }

    @GetMapping("/bands")
    public List<FrequencyBand> getFccSpectrumBands() {
        return fccApiService.getFccSpectrumBands();
    }

    @GetMapping("/validate")
    public Mono<String> validateApiConnection() {
        return fccApiService.validateApiConnection();
    }
}