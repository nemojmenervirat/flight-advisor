package com.github.nemojmenervirat.flightadvisor.payload;

import java.math.BigDecimal;
import java.util.List;

public class FlightResponse {

	private List<RouteResponse> routes;

	private BigDecimal price;
	private BigDecimal distance;
	private BigDecimal duration;

	public List<RouteResponse> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteResponse> routes) {
		this.routes = routes;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

}
