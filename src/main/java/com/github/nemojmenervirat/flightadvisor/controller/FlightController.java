package com.github.nemojmenervirat.flightadvisor.controller;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;
import com.github.nemojmenervirat.flightadvisor.payload.RouteResponse;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.service.FlightCache;
import com.github.nemojmenervirat.flightadvisor.service.FlightService;
import com.github.nemojmenervirat.flightadvisor.utils.DistanceUtils;

@RestController
public class FlightController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private FlightCache flightCache;
	@Autowired
	private FlightService flightService;
	@Autowired
	private CityRepository cityRepository;

	@GetMapping(UrlConstants.FLIGHT_CHEAPEST)
	public ResponseEntity<FlightResponse> getCheapest(@RequestParam String sourceCountry, @RequestParam String sourceCity,
			@RequestParam String destinationCountry, @RequestParam String destinationCity) {
		City source = cityRepository.findByCountryAndNameOrThrow(sourceCountry, sourceCity);
		City destination = cityRepository.findByCountryAndNameOrThrow(destinationCountry, destinationCity);
		FlightResponse cachedResponse = flightCache.get(source.getCityId(), destination.getCityId());
		if (cachedResponse != null) {
			log.info("Returning from cache.");
			return ResponseEntity.ok(cachedResponse);
		}
		List<Route> routes = flightService.getCheapestRoute(source, destination);
		if (routes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		FlightResponse response = new FlightResponse();
		response.setRoutes(new LinkedList<>());
		response.setPrice(BigDecimal.ZERO);
		response.setDistance(BigDecimal.ZERO);
		response.setDuration(BigDecimal.ZERO);
		for (Route route : routes) {
			RouteResponse routeResponse = new RouteResponse();
			routeResponse.setAirline(route.getAirline());
			routeResponse.setSourceCity(route.getSourceCity().toString());
			routeResponse.setDestinationCity(route.getDestinationCity().toString());
			routeResponse.setPrice(route.getPrice());
			routeResponse.setDistance(DistanceUtils.calclulateDistanceBetweenTwoAirports(route.getSourceAirport(), route.getDestinationAirport()));
			routeResponse.setDuration(DistanceUtils.calculateDuration(routeResponse.getDistance()));
			response.getRoutes().add(routeResponse);

			response.setPrice(response.getPrice().add(routeResponse.getPrice()));
			response.setDistance(response.getDistance().add(routeResponse.getDistance()));
			response.setDuration(response.getDuration().add(routeResponse.getDuration()));
		}
		flightCache.add(source.getCityId(), destination.getCityId(), response);
		return ResponseEntity.ok(response);
	}
}
