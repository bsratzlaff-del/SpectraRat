package com.spectrarat.spectrarat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectrarat.spectrarat.model.FrequencyBand;
import com.spectrarat.spectrarat.model.Microphone;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Tells Spring Boot to prepare a MockMvc instance
class SpectraratApplicationTests {

	@Autowired
	private MockMvc mockMvc; // The object we'll use to simulate web requests

	@Autowired
	private FrequencyBandRepository frequencyBandRepository; // To clean up the DB

	@Autowired
	private MicrophoneRepository microphoneRepository; // To clean up the DB

	@Autowired
	private ObjectMapper objectMapper; // To convert Java objects to JSON strings

	@BeforeEach
	void setUp() {
		microphoneRepository.deleteAll();
		frequencyBandRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		// Clean the database after each test to ensure tests are independent
		microphoneRepository.deleteAll();
		frequencyBandRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void whenPostFrequencyBand_thenCreateFrequencyBand() throws Exception {
		FrequencyBand band = new FrequencyBand(null, "G50", 470.0, 534.0);

		mockMvc.perform(post("/api/frequency-bands")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(band)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()) // More robust check for the ID
				.andExpect(jsonPath("$.bandName", is("G50")));
	}

	@Test
	void givenFrequencyBands_whenGetFrequencyBands_thenReturnJsonArray() throws Exception {
		frequencyBandRepository.save(new FrequencyBand(null, "H50", 518.0, 572.0));

		mockMvc.perform(get("/api/frequency-bands").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].bandName", is("H50")));
	}

	@Test
	void whenPostMicrophone_thenCreateMicrophone() throws Exception {
		Microphone mic = new Microphone();
		mic.setManufacturer("Shure");
		mic.setModelName("SM58");
		mic.setCapsuleType("Dynamic");

		mockMvc.perform(post("/api/microphones")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(mic)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()) 
				.andExpect(jsonPath("$.manufacturer", is("Shure")));
	}

	@Test
	void givenMicrophones_whenGetMicrophones_thenReturnJsonArray() throws Exception {
		Microphone mic = new Microphone();
		mic.setManufacturer("Sennheiser");
		mic.setModelName("e935");
		mic.setCapsuleType("Dynamic");
		microphoneRepository.save(mic);

		mockMvc.perform(get("/api/microphones").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].manufacturer", is("Sennheiser")));
	}
}
