package com.github.nemojmenervirat.flightadvisor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;
import com.github.nemojmenervirat.flightadvisor.service.FlightService;

@RestController
public class FlightController {

	@Autowired
	private FlightService flightService;

	@GetMapping(UrlConstants.FLIGHT_CHEAPEST)
	public ResponseEntity<FlightResponse> getCheapest(@RequestParam Long sourceCityId, @RequestParam Long destinationCityId) {
		FlightResponse flightResponse = flightService.getCheapestRoute(sourceCityId, destinationCityId);
		if (flightResponse == null || flightResponse.getRoutes().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(flightResponse);
	}
}
