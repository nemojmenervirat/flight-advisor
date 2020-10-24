package com.github.nemojmenervirat.flightadvisor.payload;

import java.math.BigDecimal;
import java.util.List;

public class FlightResponse {

	private List<RouteResponse> routes;

	private BigDecimal price;
	private BigDecimal length;

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

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

}
