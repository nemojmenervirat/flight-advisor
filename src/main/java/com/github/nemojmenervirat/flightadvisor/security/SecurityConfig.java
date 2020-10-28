package com.github.nemojmenervirat.flightadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.nemojmenervirat.flightadvisor.controller.UrlConstants;
import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final AppUserService appUserService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, AppUserService appUserService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.appUserService = appUserService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		//@formatter:off
				// permit for all
				// POST (signUp, login)
				.antMatchers(HttpMethod.POST,
						UrlConstants.SIGN_UP,
						UrlConstants.LOGIN).permitAll()
				// permit for administrator
				// POST (addCity, importCities, importAirports, importRoutes)
				.antMatchers(HttpMethod.POST,
						UrlConstants.CITIES,
						UrlConstants.CITIES_IMPORT,
						UrlConstants.AIRPORTS_IMPORT,
						UrlConstants.ROUTES_IMPORT,
						UrlConstants.APP_RESET).hasAuthority(Role.ADMIN.getValue())
				// DELETE (removeCity)
				.antMatchers(HttpMethod.DELETE,
						UrlConstants.CITY).hasAuthority(Role.ADMIN.getValue())
				// permit for user
				// GET (getCities, getCheapestFlight)
				.antMatchers(HttpMethod.GET,
						UrlConstants.CITIES,
						UrlConstants.FLIGHT_CHEAPEST).hasAuthority(Role.USER.getValue())
				// POST (addComment)
				.antMatchers(HttpMethod.POST,
						UrlConstants.CITY_COMMENTS).hasAuthority(Role.USER.getValue())
				// PUT (changeComment)
				.antMatchers(HttpMethod.PUT,
						UrlConstants.CITY_COMMENT).hasAuthority(Role.USER.getValue())
				// DELETE (removeComment)
				.antMatchers(HttpMethod.DELETE,
						UrlConstants.CITY_COMMENT).hasAnyAuthority(Role.USER.getValue())
				.and()
				.csrf().disable()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), appUserService))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				//@formatter:on
		// .anyRequest().permitAll().and().csrf().disable();
	}
}