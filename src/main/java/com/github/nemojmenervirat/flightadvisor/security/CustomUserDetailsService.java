package com.github.nemojmenervirat.flightadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

@Primary
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserService appUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserService.findOneByUsernameIgnoreCase(username).map(CustomPrincipal::create)
				.orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));
	}
}