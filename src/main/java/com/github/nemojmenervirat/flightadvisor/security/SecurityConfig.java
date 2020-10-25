package com.github.nemojmenervirat.flightadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
		String[] adminPostEndpoints = { UrlConstants.CITIES, UrlConstants.CITIES_IMPORT };
		String[] adminDeleteEndpoints = { UrlConstants.CITY };
		String[] userGetEndpoints = { UrlConstants.CITIES };
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, UrlConstants.SIGN_UP, UrlConstants.LOGIN).permitAll()
				.antMatchers(HttpMethod.POST, adminPostEndpoints).hasAuthority(Role.ADMIN.getValue())
				.antMatchers(HttpMethod.DELETE, adminDeleteEndpoints).hasAuthority(Role.ADMIN.getValue())
				.antMatchers(HttpMethod.GET, userGetEndpoints).hasAuthority(Role.USER.getValue())
				// .anyRequest().permitAll().and().csrf().disable();
				.and()
				.csrf().disable()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), appUserService))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}

}