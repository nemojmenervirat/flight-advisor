package com.github.nemojmenervirat.flightadvisor.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
