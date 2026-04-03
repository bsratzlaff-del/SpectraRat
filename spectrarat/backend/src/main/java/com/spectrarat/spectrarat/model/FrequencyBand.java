package com.spectrarat.spectrarat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FrequencyBand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bandName;

    @Column(name = "min_frequency")
    private double minFrequency;

    @Column(name = "max_frequency")
    private double maxFrequency;

    // Constructor for tests and convenience
    public FrequencyBand() {}

    public FrequencyBand(Long id, String bandName, double minFrequency, double maxFrequency) {
        this.id = id;
        this.bandName = bandName;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public double getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(double minFrequency) {
        this.minFrequency = minFrequency;
    }

    public double getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(double maxFrequency) {
        this.maxFrequency = maxFrequency;
    }
}