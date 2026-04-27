package com.spectrarat.spectrarat.controller; // Update this if your package structure is different

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TelemetryTestController {

    @GetMapping("/error")
    public String triggerError() {
        // This will show up in your Azure Application Insights 'Failures' tab
        throw new RuntimeException("Telemetry Test: " + java.time.LocalDateTime.now());
    }
}