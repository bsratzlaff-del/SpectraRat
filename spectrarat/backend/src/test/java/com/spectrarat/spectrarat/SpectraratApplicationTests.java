package com.spectrarat.spectrarat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spectrarat.spectrarat.repository.CustomerRepository;
import com.spectrarat.spectrarat.repository.FrequencyBandRepository;
import com.spectrarat.spectrarat.repository.MicrophoneRepository;
import com.spectrarat.spectrarat.repository.ReceiverRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Tells Spring Boot to prepare a MockMvc instance
class SpectraratApplicationTests {

	@Autowired
	private MockMvc mockMvc; // The object we'll use to simulate web requests

	@Autowired
	private FrequencyBandRepository frequencyBandRepository; // To clean up the DB

	@Autowired
	private MicrophoneRepository microphoneRepository; // To clean up the DB

	@Autowired
	private ReceiverRepository receiverRepository; // To clean up the DB

	@Autowired
	private CustomerRepository customerRepository; // To clean up the DB

	@BeforeEach
	void setUp() {
		customerRepository.deleteAll();
		receiverRepository.deleteAll();
		microphoneRepository.deleteAll();
		frequencyBandRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		// Clean the database after each test to ensure tests are independent
		customerRepository.deleteAll();
		receiverRepository.deleteAll();
		microphoneRepository.deleteAll();
		frequencyBandRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void whenPostFrequencyBand_thenCreateFrequencyBand() throws Exception {
		String bandJson = "{\"bandName\":\"G50\",\"lowerFrequency\":470.0,\"upperFrequency\":534.0}";

		mockMvc.perform(post("/api/frequency-bands")
					.contentType(MediaType.APPLICATION_JSON)
					.content(bandJson))
				.andExpect(status().isCreated()) // Expect 201 Created
				.andExpect(jsonPath("$.id").exists()) // More robust check for the ID
				.andExpect(jsonPath("$.bandName", is("G50")));
	}

	@Test
	void givenFrequencyBands_whenGetFrequencyBands_thenReturnJsonArray() throws Exception {
		String bandJson = "{\"bandName\":\"H50\",\"lowerFrequency\":518.0,\"upperFrequency\":572.0}";

		mockMvc.perform(post("/api/frequency-bands")
					.contentType(MediaType.APPLICATION_JSON)
					.content(bandJson))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/frequency-bands").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].bandName", is("H50")));
	}

	@Test
	void whenPostMicrophone_thenCreateMicrophone() throws Exception {
		String micJson = "{\"manufacturer\":\"Shure\",\"modelName\":\"SM58\",\"capsuleType\":\"Dynamic\"}";

		mockMvc.perform(post("/api/microphones")
					.contentType(MediaType.APPLICATION_JSON)
					.content(micJson))
				.andExpect(status().isCreated()) // Expect 201 Created
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.manufacturer", is("Shure")));
	}

	@Test
	void givenMicrophones_whenGetMicrophones_thenReturnJsonArray() throws Exception {
		String micJson = "{\"manufacturer\":\"Sennheiser\",\"modelName\":\"e935\",\"capsuleType\":\"Dynamic\"}";

		mockMvc.perform(post("/api/microphones")
					.contentType(MediaType.APPLICATION_JSON)
					.content(micJson))
				.andExpect(status().isCreated());
	}
}