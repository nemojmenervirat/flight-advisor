package com.github.nemojmenervirat.flightadvisor.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CommentRequest {

	@NotEmpty
	@Size(min = 10, max = 300)
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
