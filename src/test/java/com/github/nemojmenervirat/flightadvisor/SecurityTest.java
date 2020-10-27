package com.github.nemojmenervirat.flightadvisor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nemojmenervirat.flightadvisor.controller.UrlConstants;
import com.github.nemojmenervirat.flightadvisor.model.City;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class SecurityTest {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeAll
	public void before() {
		log.debug("Before...");
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void contextLoads() {
		assertThat(mockMvc).isNotNull();
		assertThat(objectMapper).isNotNull();
	}

	@WithMockUser(username = "admin", roles = "ADMIN")
	@Test
	public void adminAddCity_expectSuccess200() throws Exception {
		City city = new City();
		city.setName("Trebinje");
		city.setCountry("BiH");
		city.setDescription("Lijep grad");
		String cityJson = objectMapper.writeValueAsString(city);
		mockMvc.perform(post(UrlConstants.CITIES).contentType(MediaType.APPLICATION_JSON).content(cityJson)).andExpect(status().isOk());
	}

}
