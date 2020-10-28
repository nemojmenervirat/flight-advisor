package com.github.nemojmenervirat.flightadvisor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name", "country" })
})
public class City {

	@Id
	@GeneratedValue
	private Long cityId;

	@Column(length = 50)
	private String name;

	@Column(length = 50)
	private String country;

	@Column(length = 250)
	private String description;

	@Transient
	private List<Comment> comments;

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof City) {
			City o = (City) obj;
			return country.equals(o.country) && name.equals(o.name);
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return name + ", " + country;
	}

}
