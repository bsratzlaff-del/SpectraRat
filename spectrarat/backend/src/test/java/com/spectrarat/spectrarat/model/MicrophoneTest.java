package com.spectrarat.spectrarat.model; // Ensure this matches your actual package name

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MicrophoneTest {

    @Test
    void verifyHardwareInheritance() {
        Microphone testMic = new Microphone();
        testMic.setModelName("Shure AD2"); // Inherited attribute
        testMic.setCost(899.00);           // Inherited attribute
        
        assertThat(testMic.getModelName()).isEqualTo("Shure AD2");
        assertThat(testMic.getCost()).isGreaterThan(0);
    }
    
}