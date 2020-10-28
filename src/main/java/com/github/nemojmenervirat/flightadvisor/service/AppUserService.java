package com.github.nemojmenervirat.flightadvisor.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;

import com.github.nemojmenervirat.flightadvisor.model.AppUser;
import com.github.nemojmenervirat.flightadvisor.payload.SignUpRequest;
import com.github.nemojmenervirat.flightadvisor.security.Role;

public interface AppUserService {

	public void add(SignUpRequest request, Role role);

	public String getSaltedPassword(String username, String password);

	public Collection<? extends GrantedAuthority> getGrantedAuthorities(String username);

	public Optional<AppUser> findOneByUsernameIgnoreCase(String username);
}
