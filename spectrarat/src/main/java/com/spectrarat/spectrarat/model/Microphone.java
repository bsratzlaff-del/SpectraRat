package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "microphones")
@Getter
@Setter
@NoArgsConstructor
// Inheritance: Microphone "is a" WirelessDevice
public class Microphone extends WirelessDevice {
    
    private String capsuleType;

    // Polymorphism: Overriding the abstract method from the parent class
    @Override
    public String getDeviceCategory() {
        return "Transmitter / Microphone";
    }
}