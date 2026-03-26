package com.spectrarat.spectrarat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpectraratApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpectraratApplication.class, args);

		// Add shutdown hook for graceful shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down Spectrarat Application...");
			context.close();
		}));
	}

}
