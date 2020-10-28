package com.github.nemojmenervirat.flightadvisor.service;

import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;

public interface FlightService {

	public FlightResponse getCheapestRoute(Long sourceCityId, Long destinationCityId);

}
