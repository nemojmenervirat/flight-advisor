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
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nemojmenervirat.flightadvisor.controller.UrlConstants;
import com.github.nemojmenervirat.flightadvisor.payload.LoginRequest;
import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AppUserControllerTests {

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

	@Test
	void adminLogin_expectSuccess200() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("admin");
		loginRequest.setPassword("admin");
		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);
		mockMvc.perform(post(UrlConstants.LOGIN).contentType(MediaType.APPLICATION_JSON).content(loginRequestJson)).andExpect(status().isOk());
	}

	@Test
	void userSignUpNoLastName_ExpectFail400() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setFirstName("milan");
		signUpRequest.setUsername("milan");
		signUpRequest.setPassword("milan");
		String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);
		mockMvc.perform(post(UrlConstants.SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(signUpRequestJson)).andExpect(status().isBadRequest());
	}

	@Test
	void userSignUp_ExpectSuccess200() throws Exception {
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setFirstName("milan");
		signUpRequest.setLastName("djurica");
		signUpRequest.setUsername("milan");
		signUpRequest.setPassword("milan");
		String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);
		System.out.println("SIGN UP REQUEST JSON = " + signUpRequestJson);
		mockMvc.perform(post(UrlConstants.SIGN_UP).contentType(MediaType.APPLICATION_JSON).content(signUpRequestJson)).andExpect(status().isOk());
	}
}
