package com.github.nemojmenervirat.flightadvisor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.nemojmenervirat.flightadvisor.security.Role;

@Entity
@Table
public class AppUser {

	@Id
	@GeneratedValue
	private Long appUserId;

	@Column(unique = true, length = 20)
	private String username;

	private String passwordHash;
	private String salt;

	@Column(length = 20)
	private String firstName;

	@Column(length = 20)
	private String lastName;

	@Enumerated
	private Role role;

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AppUser) {
			return appUserId == ((AppUser) obj).appUserId;
		}
		return super.equals(obj);
	}

}
