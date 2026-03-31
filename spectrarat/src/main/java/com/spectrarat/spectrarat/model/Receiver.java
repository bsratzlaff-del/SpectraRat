package com.spectrarat.spectrarat.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "receivers")
@ToString(exclude = {"compatibleMicrophones", "availableFrequencyBands"})
@NoArgsConstructor
public class Receiver extends WirelessDevice {

    private String receiverType;

    @ManyToMany
    @JoinTable(
        name = "receiver_microphones",
        joinColumns = @JoinColumn(name = "receiver_id"),
        inverseJoinColumns = @JoinColumn(name = "microphone_id")
    )
    private List<Microphone> compatibleMicrophones = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "receiver_frequency_bands",
        joinColumns = @JoinColumn(name = "receiver_id"),
        inverseJoinColumns = @JoinColumn(name = "frequency_band_id")
    )
    private List<FrequencyBand> availableFrequencyBands = new ArrayList<>();

    public double calculateBundleCost(Microphone microphone) {
        if (microphone == null) {
            return getCost();
        }
        return getCost() + microphone.getCost();
    }

    public double calculateBundleCost(List<Microphone> microphones) {
        double base = getCost();
        if (microphones == null || microphones.isEmpty()) {
            return base;
        }
        for (Microphone m : microphones) {
            if (m != null) {
                base += m.getCost();
            }
        }
        return base;
    }

    @Override
    public String getDeviceCategory() {
        return "Receiver";
    }

    // Check if the receiver's frequency band is within legal limits
    public boolean checkLegalStatus() {
        if (getFrequencyBand() != null && getFrequencyBand().getMaxFrequency() > 614.0 && getFrequencyBand().getMinFrequency() < 698.0) {
            return false; // Falls within restricted US bands
        }
        return true;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public List<Microphone> getCompatibleMicrophones() {
        return compatibleMicrophones;
    }

    public List<FrequencyBand> getAvailableFrequencyBands() {
        return availableFrequencyBands;
    }
}
