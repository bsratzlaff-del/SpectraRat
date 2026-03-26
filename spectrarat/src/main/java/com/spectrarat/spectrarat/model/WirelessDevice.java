package com.spectrarat.spectrarat.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class WirelessDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Encapsulation: Private fields
    private String modelName;
    private String manufacturer;
    private double cost;

    @ManyToOne
    @JoinColumn(name = "frequency_band_id")
    private FrequencyBand frequencyBand;
    // Polymorphism: Abstract method to be overridden by subclasses
    public abstract String getDeviceCategory();
}