package com.spectrarat.spectrarat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "microphones")
@Getter
@Setter
@ToString
@NoArgsConstructor
// Inheritance: Microphone "is a" WirelessDevice
public class Microphone extends WirelessDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String capsuleType;

    
    // Polymorphism: Overriding the abstract method from the parent class
    @Override
    public String getDeviceCategory() {
        return "Transmitter / Microphone";
    }
}