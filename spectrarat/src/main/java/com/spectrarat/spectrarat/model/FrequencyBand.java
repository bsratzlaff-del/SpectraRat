package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "frequency_bands")
@ToString
@NoArgsConstructor
public class FrequencyBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bandName; // e.g., "G50", "A1"
    private double minFreq;
    private double maxFreq;

    // Explicit getters for minFreq and maxFreq to ensure compiler recognition
    public double getMinFreq() {
        return minFreq;
    }

    public double getMaxFreq() {
        return maxFreq;
    }

    public Long getId() {
        return id;
    }

    public String getBandName() {
        return bandName;
    }

    // Explicit AllArgsConstructor to ensure compiler always finds it
    public FrequencyBand(Long id, String bandName, double minFreq, double maxFreq) {
        this.id = id;
        this.bandName = bandName;
        this.minFreq = minFreq;
        this.maxFreq = maxFreq;
    }
}