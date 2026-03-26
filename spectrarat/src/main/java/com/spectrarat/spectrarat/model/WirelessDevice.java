package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wireless_devices")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class WirelessDevice {
    
    // Encapsulation: Private fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelName;
    private String manufacturer;

    @ManyToOne
    @JoinColumn(name = "frequency_band_id")
    private FrequencyBand frequencyBand;

    // Polymorphism: Abstract method to be overridden by subclasses
    public abstract String getDeviceCategory();

    // Polymorphism: A standard method that subclasses could optionally override
    public boolean checkLegalStatus() {
        // Now uses the related FrequencyBand object for its logic
        if (frequencyBand != null && frequencyBand.getMaxFreq() > 614.0 && frequencyBand.getMinFreq() < 698.0) {
            return false; // Falls within restricted US bands
        }
        return true;
    }
}