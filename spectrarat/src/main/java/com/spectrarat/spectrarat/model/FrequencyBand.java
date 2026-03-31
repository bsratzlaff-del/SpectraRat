package com.spectrarat.spectrarat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public double getMinFrequency() {
        return minFrequency;
    }
    public double getMaxFrequency() {
        return maxFrequency;
    }
}