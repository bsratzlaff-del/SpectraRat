package com.spectrarat.spectrarat.model;

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