package com.spectrarat.spectrarat.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecommendationResult {
    private String status;
    private double matchPercentage;
    private String modelName;
    private String description;
    private String timestamp;

    public RecommendationResult() {}

    public RecommendationResult(String status, double matchPercentage, String modelName, String description) {
        this.status = status;
        this.matchPercentage = matchPercentage;
        this.modelName = modelName;
        this.description = description;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}