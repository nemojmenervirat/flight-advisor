package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.AppUser;
import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.repository.AppUserRepository;
import com.github.nemojmenervirat.flightadvisor.security.Role;
import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

@Service
class AppUserServiceImpl implements AppUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public void add(SignUpRequest request, Role role) {
		if (appUserRepository.findOneByUsernameIgnoreCase(request.getUsername()).isPresent()) {
			throw new CustomException("Username [" + request.getUsername() + "] is not available!");
		}
		AppUser appUser = new AppUser();
		appUser.setFirstName(request.getFirstName());
		appUser.setLastName(request.getLastName());
		appUser.setUsername(request.getUsername());
		appUser.setSalt(UUID.randomUUID().toString());
		appUser.setPasswordHash(passwordEncoder.encode(request.getPassword() + appUser.getSalt()));
		appUser.setRole(role);
		appUserRepository.save(appUser);
	}

	@Override
	public String getSaltedPassword(String username, String password) {
		AppUser appUser = appUserRepository.findOneByUsernameIgnoreCase(username).orElseThrow(() -> new CustomException("User not found"));
		return password + appUser.getSalt();
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
		AppUser appUser = appUserRepository.findOneByUsernameIgnoreCase(username).orElseThrow(() -> new CustomException("User not found"));
		return Arrays.asList(new SimpleGrantedAuthority(appUser.getRole().getValue()));
	}

	@Override
	public Optional<AppUser> findOneByUsernameIgnoreCase(String username) {
		return appUserRepository.findOneByUsernameIgnoreCase(username);
	}

}
