package com.github.nemojmenervirat.flightadvisor.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CityRequest {

	@NotEmpty
	@Size(max = 50)
	private String name;
	@NotEmpty
	@Size(max = 50)
	private String country;
	@NotEmpty
	@Size(max = 250)
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name + ", " + country;
	}
}
