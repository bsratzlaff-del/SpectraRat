package com.spectrarat.spectrarat.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class WirelessDevice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // Encapsulation: Private fields
    protected String modelName;
    protected String manufacturer;
    protected double cost;

    @ManyToOne
    @JoinColumn(name = "frequency_band_id")
    protected FrequencyBand frequencyBand;
    // Polymorphism: Abstract method to be overridden by subclasses
    public abstract String getDeviceCategory();

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public FrequencyBand getFrequencyBand() {
        return frequencyBand;
    }
    public void setFrequencyBand(FrequencyBand frequencyBand) {
        this.frequencyBand = frequencyBand;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
}