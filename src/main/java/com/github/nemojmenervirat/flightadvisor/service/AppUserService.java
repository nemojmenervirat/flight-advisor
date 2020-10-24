package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.AppUser;
import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.repository.AppUserRepository;
import com.github.nemojmenervirat.flightadvisor.security.Role;

@Service
public class AppUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AppUserRepository appUserRepository;

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

	public List<AppUser> getAll() {
		return appUserRepository.findAll();
	}

}
