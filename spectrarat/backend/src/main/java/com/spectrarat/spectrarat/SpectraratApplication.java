package com.spectrarat.spectrarat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

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
                        .allowedOrigins("http://localhost:4200") // The Angular dev server
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
