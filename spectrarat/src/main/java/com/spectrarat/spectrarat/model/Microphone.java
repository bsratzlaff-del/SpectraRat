package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "microphones")
@ToString
@NoArgsConstructor
// Inheritance: Microphone "is a" WirelessDevice
public class Microphone extends WirelessDevice {

    private String capsuleType;

    // Polymorphism: Overriding the abstract method from the parent class
    @Override
    public String getDeviceCategory() {
        return "Transmitter / Microphone";
    }

    public String getCapsuleType() {
        return capsuleType;
    }

    public void setCapsuleType(String capsuleType) {
        this.capsuleType = capsuleType;
    }
}