package com.github.nemojmenervirat.flightadvisor.service;

import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;

public interface FlightServiceCache {

	public FlightResponse getFlightResponse(long sourceCityId, long destinationCityId);

	public void addFlightResponse(long sourceCityId, long destinationCityId, FlightResponse flightResponse);

	public void clear();

}
