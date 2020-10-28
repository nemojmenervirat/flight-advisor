package com.github.nemojmenervirat.flightadvisor.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Route {

	@Id
	@GeneratedValue
	private Long routeId;

	private String airline;
	private Long airlineId;

	@ManyToOne
	@JoinColumn(name = "airport_id_source")
	private Airport sourceAirport;

	@ManyToOne
	@JoinColumn(name = "airport_id_destination")
	private Airport destinationAirport;

	private BigDecimal price;

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public Long getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(Long airlineId) {
		this.airlineId = airlineId;
	}

	public Airport getSourceAirport() {
		return sourceAirport;
	}

	public void setSourceAirport(Airport sourceAirport) {
		this.sourceAirport = sourceAirport;
	}

	public Airport getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(Airport destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public City getSourceCity() {
		return sourceAirport.getCity();
	}

	public City getDestinationCity() {
		return destinationAirport.getCity();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
