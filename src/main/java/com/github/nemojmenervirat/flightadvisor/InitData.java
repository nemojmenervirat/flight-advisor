package com.github.nemojmenervirat.flightadvisor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.security.Role;
import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

@Service
public class InitData {

	@Autowired
	private AppUserService appUserService;

	@PostConstruct
	public void init() {
		SignUpRequest request = new SignUpRequest();
		request.setUsername("admin");
		request.setPassword("admin");
		request.setFirstName("Administrator");
		request.setLastName("Administrator");
		appUserService.add(request, Role.ADMIN);
	}

}
