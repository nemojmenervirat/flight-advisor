package com.github.nemojmenervirat.flightadvisor.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.nemojmenervirat.flightadvisor.model.AppUser;
import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.security.Role;
import com.github.nemojmenervirat.flightadvisor.service.AppUserService;

@RestController
public class AppUserController {

	@Autowired
	private AppUserService appUserService;

	@PostMapping(UrlConstants.SIGN_UP)
	public void signUp(@RequestBody @Valid SignUpRequest request) {
		appUserService.add(request, Role.USER);
	}

	@GetMapping
	public List<AppUser> getUsers() {
		return appUserService.getAll();
	}

}
