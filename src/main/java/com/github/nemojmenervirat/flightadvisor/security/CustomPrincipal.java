package com.github.nemojmenervirat.flightadvisor.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.nemojmenervirat.flightadvisor.model.AppUser;

public class CustomPrincipal implements UserDetails {

	private final AppUser appUser;
	private final Collection<? extends GrantedAuthority> authorities;

	private CustomPrincipal(AppUser appUser) {
		this.appUser = appUser;
		this.authorities = Arrays.asList(new SimpleGrantedAuthority(appUser.getRole().getValue()));
	}

	public static CustomPrincipal create(AppUser appUser) {
		return new CustomPrincipal(appUser);
	}

	public AppUser getAppUser() {
		return appUser;
	}

	@Override
	public String getUsername() {
		return appUser.getUsername();
	}

	@Override
	public String getPassword() {
		return appUser.getPasswordHash();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CustomPrincipal) {
			return ((CustomPrincipal) o).appUser == this.appUser;
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return appUser.hashCode();
	}

}
