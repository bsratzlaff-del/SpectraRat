package com.spectrarat.spectrarat;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.model.Receiver;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import com.spectrarat.spectrarat.repository.ReceiverRepository;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(FrequencyBandRepository frequencyBandRepository,
                                   MicrophoneRepository microphoneRepository,
                                   ReceiverRepository receiverRepository) {
        return args -> {
            // Clear existing data (optional, useful for 'create' ddl-auto)
            // Clear many-to-many relationships first
            List<Receiver> allReceivers = receiverRepository.findAll();
            for (Receiver receiver : allReceivers) {
                receiver.getCompatibleMicrophones().clear();
                receiver.getAvailableFrequencyBands().clear();
                receiverRepository.save(receiver);
            }
            receiverRepository.deleteAll();
            microphoneRepository.deleteAll();
            frequencyBandRepository.deleteAll();

            // 1. Add Frequency Bands
            FrequencyBand g50 = new FrequencyBand(null, "G50", 470.0, 534.0);
            FrequencyBand g57p = new FrequencyBand(null, "G57+", 470.0, 616.0);
            FrequencyBand g58 = new FrequencyBand(null, "G58", 470.0, 514.0);
            FrequencyBand h9 = new FrequencyBand(null, "H9", 512.0, 542.0);
            FrequencyBand h10 = new FrequencyBand(null, "H10", 542.0, 572.0);
            FrequencyBand h11 = new FrequencyBand(null, "H11", 572.0, 596.0);
            FrequencyBand h50 = new FrequencyBand(null, "H50", 518.0, 572.0);
            FrequencyBand h55 = new FrequencyBand(null, "H55", 512.5, 554.5);
            FrequencyBand j11 = new FrequencyBand(null, "J11", 596.0, 616.0);
            FrequencyBand j50 = new FrequencyBand(null, "J50", 572.0, 608.0);
            FrequencyBand j50a = new FrequencyBand(null, "J50A", 572.0, 616.0);
            FrequencyBand j52 = new FrequencyBand(null, "J52", 559.0, 616.0);
            FrequencyBand k54 = new FrequencyBand(null, "K54", 608.0, 663.0);
            FrequencyBand v50 = new FrequencyBand(null, "V50", 174.0, 216.0);
            FrequencyBand l50a = new FrequencyBand(null, "L50A", 653.0, 663.0);

            frequencyBandRepository.save(g50);
            frequencyBandRepository.save(g57p);
            frequencyBandRepository.save(g58);
            frequencyBandRepository.save(h9);
            frequencyBandRepository.save(h10);
            frequencyBandRepository.save(h11);
            frequencyBandRepository.save(h50);
            frequencyBandRepository.save(h55);
            frequencyBandRepository.save(j11);
            frequencyBandRepository.save(j50);
            frequencyBandRepository.save(j50a);
            frequencyBandRepository.save(j52);
            frequencyBandRepository.save(k54);
            frequencyBandRepository.save(v50);
            frequencyBandRepository.save(l50a);

            System.out.println("Initialized Frequency Bands:");
            frequencyBandRepository.findAll().forEach(System.out::println);


            // 2. Add Microphones, linking them to Frequency Bands
            Microphone sm58 = new Microphone();
            sm58.setManufacturer("Shure");
            sm58.setModelName("SM58");
            sm58.setCost(99.00);

            Microphone beta87 = new Microphone();
            beta87.setManufacturer("Shure");
            beta87.setModelName("Beta87");
            beta87.setCost(249.00);
            
            Microphone ksm8 = new Microphone();
            ksm8.setManufacturer("Shure");
            ksm8.setModelName("KSM8");
            ksm8.setCost(399.00);

            microphoneRepository.save(sm58);
            microphoneRepository.save(beta87);
            microphoneRepository.save(ksm8);

            System.out.println("\nInitialized Microphones:");
            microphoneRepository.findAll().forEach(System.out::println);


            // 3. Add receivers, linking to frequency band/mic type/cost
            Receiver rx1 = new Receiver();
            rx1.setManufacturer("Shure");
            rx1.setModelName("BLX");
            rx1.setReceiverType("Single Channel");
            rx1.getCompatibleMicrophones().add(sm58);
            rx1.setCost(469.00);
            rx1.setFrequencyBand(h9);
            rx1.getAvailableFrequencyBands().add(h10);
            rx1.getAvailableFrequencyBands().add(h11);
            rx1.getAvailableFrequencyBands().add(j11);


            Receiver rx2 = new Receiver();
            rx2.setManufacturer("Shure");
            rx2.setModelName("SLX-D");
            rx2.setReceiverType("Single Channel");
            rx2.getCompatibleMicrophones().add(sm58);
            rx2.getCompatibleMicrophones().add(beta87);
            rx2.setCost(989.00);
            rx2.setFrequencyBand(g58);
            rx2.getAvailableFrequencyBands().add(h55);
            rx2.getAvailableFrequencyBands().add(j52);
            

            Receiver rx3 = new Receiver();
            rx3.setManufacturer("Shure");
            rx3.setModelName("ULX-D");
            rx3.setReceiverType("Quad Channel");

            rx3.getCompatibleMicrophones().add(sm58);
            rx3.getCompatibleMicrophones().add(beta87);
            rx3.getCompatibleMicrophones().add(ksm8);   
            rx3.setCost(6776.00);
            rx3.setFrequencyBand(g50);
            rx3.getAvailableFrequencyBands().add(v50);
            rx3.getAvailableFrequencyBands().add(h50);
            rx3.getAvailableFrequencyBands().add(j50a);

            Receiver rx4 = new Receiver();
            rx4.setManufacturer("Shure");
            rx4.setModelName("Axient Digital");
            rx4.setReceiverType("Quad Channel");

            rx4.getCompatibleMicrophones().add(beta87);
            rx4.getCompatibleMicrophones().add(ksm8);   
            rx4.setCost(7086.00);
            rx4.setFrequencyBand(g57p);
            rx4.getAvailableFrequencyBands().add(k54);


            double rx1Bundle = rx1.calculateBundleCost(sm58);
            double rx2Bundle = rx2.calculateBundleCost(sm58);
            double rx3Bundle = rx3.calculateBundleCost(beta87);
            double rx4Bundle = rx4.calculateBundleCost(ksm8);

            System.out.println("\nBundle pricing examples:");
            System.out.println(rx1.getModelName() + " + " + sm58.getModelName() + " = " + rx1Bundle);
            System.out.println(rx2.getModelName() + " + " + sm58.getModelName() + " = " + rx2Bundle);
            System.out.println(rx3.getModelName() + " + " + beta87.getModelName() + " = " + rx3Bundle);
            System.out.println(rx4.getModelName() + " + " + ksm8.getModelName() + " = " + rx4Bundle);

            receiverRepository.save(rx1);
            receiverRepository.save(rx2);
            receiverRepository.save(rx3);
            receiverRepository.save(rx4);

            System.out.println("\nInitialized Receivers:");
            receiverRepository.findAll().forEach(System.out::println);

            // 4. For Customer data, you would inject a CustomerRepository and add similar logic.
        };
    }
}