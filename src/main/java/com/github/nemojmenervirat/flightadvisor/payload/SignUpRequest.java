package com.github.nemojmenervirat.flightadvisor.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SignUpRequest {

	@NotEmpty
	@Size(max = 20)
	private String username;
	@NotEmpty
	@Size(max = 20)
	private String password;
	@NotEmpty
	@Size(max = 20)
	private String firstName;
	@NotEmpty
	@Size(max = 20)
	private String lastName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
