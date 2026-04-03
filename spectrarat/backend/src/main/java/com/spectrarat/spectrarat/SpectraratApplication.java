package com.spectrarat.spectrarat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.spectrarat")
public class SpectraratApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpectraratApplication.class, args);

		// Add shutdown hook for graceful shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down Spectrarat Application...");
			context.close();
		}));
	}
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Applies to any endpoint starting with /api/
                        .allowedOrigins("http://158.23.58.74:4200") // The Angular dev server
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
