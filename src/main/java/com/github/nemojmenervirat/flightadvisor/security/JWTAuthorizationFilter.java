package com.github.nemojmenervirat.flightadvisor.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		log.info("FILTER INTERNAL");

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	// Reads the JWT from the Authorization header, and then uses JWT to validate
	// the token
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		log.info("GET AUTHENTICATION");
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		log.info(token);

		if (token != null) {

			DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
					.build()
					.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

			String username = decoded.getSubject();
			String role = decoded.getClaim(Role.class.getName()).asString();
			log.info("User " + username + " Role " + role);

			if (username != null) {
				return new UsernamePasswordAuthenticationToken(username, null, Arrays.asList(new SimpleGrantedAuthority(role)));
			}

			return null;
		}

		return null;
	}
}