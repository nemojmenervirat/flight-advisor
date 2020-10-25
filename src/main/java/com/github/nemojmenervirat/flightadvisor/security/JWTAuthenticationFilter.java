package com.github.nemojmenervirat.flightadvisor.security;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nemojmenervirat.flightadvisor.controller.UrlConstants;
import com.github.nemojmenervirat.flightadvisor.payload.LoginRequest;
import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private AuthenticationManager authenticationManager;
	private AppUserService appUserService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AppUserService appUserService) {
		log.info("CONSTRUCTOR");

		this.authenticationManager = authenticationManager;
		this.appUserService = appUserService;

		setFilterProcessesUrl(UrlConstants.LOGIN);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		log.info("ATTEMPT AUTHENTICATION");
		try {
			LoginRequest loginRequest = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

			log.info("LoginRequest " + loginRequest.getUsername() + " " + loginRequest.getPassword());

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							appUserService.getSaltedPassword(loginRequest.getUsername(), loginRequest.getPassword()),
							new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
		log.info("SUCCESSFUL AUTHENTICATION");
		CustomPrincipal userDetails = (CustomPrincipal) auth.getPrincipal();
		String token = JWT.create()
				.withSubject(userDetails.getUsername())
				.withClaim(Role.class.getName(), userDetails.getAppUser().getRole().getValue())
				.withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(SecurityConstants.EXPIRATION_TIME_MINUTES)))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

		res.getWriter().write(token);
		res.getWriter().flush();
	}
}
