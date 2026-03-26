package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "frequency_bands")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bandName; // e.g., "G50", "A1"
    private double minFreq;
    private double maxFreq;
}