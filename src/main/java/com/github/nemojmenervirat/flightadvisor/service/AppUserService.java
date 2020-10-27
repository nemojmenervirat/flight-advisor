package com.github.nemojmenervirat.flightadvisor.service;

import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.security.Role;

public interface AppUserService {

	public void add(SignUpRequest request, Role role);

	public String getSaltedPassword(String username, String password);
}
