package com.github.nemojmenervirat.flightadvisor.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.nemojmenervirat.flightadvisor.model.AppUser;

public class SecurityUtils {

	public static AppUser getAppUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isUserLoggedIn(authentication)) {
			CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
			return customPrincipal.getAppUser();
		} else {
			return null;
		}
	}

	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
	}

}
