package com.github.nemojmenervirat.flightadvisor.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.nemojmenervirat.flightadvisor.model.AppUser;

public class SecurityUtils {

	public static String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	public static boolean isCurrent(AppUser appUser) {
		return appUser.getUsername().toUpperCase().equals(SecurityUtils.getUsername().toUpperCase());
	}

}
