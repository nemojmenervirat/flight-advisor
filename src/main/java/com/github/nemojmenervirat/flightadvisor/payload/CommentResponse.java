package com.github.nemojmenervirat.flightadvisor.payload;

import java.time.LocalDateTime;

public class CommentResponse {

	private Long commentId;
	private String description;
	private LocalDateTime modified;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

}
