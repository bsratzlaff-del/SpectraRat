package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
    private double minFrequency;
    private double maxFrequency;

    // Polymorphism: Abstract method to be overridden by subclasses
    public abstract String getDeviceCategory();

    // Polymorphism: A standard method that subclasses could optionally override
    public boolean checkLegalStatus() {
        // Basic FCC compliance check example (e.g., checking against the illegal 600MHz band)
        if (maxFrequency > 614.0 && minFrequency < 698.0) {
            return false; // Falls within restricted US bands
        }
        return true;
    }
}