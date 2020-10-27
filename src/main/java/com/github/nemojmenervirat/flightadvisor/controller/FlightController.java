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
import com.github.nemojmenervirat.flightadvisor.service.CityService;
import com.github.nemojmenervirat.flightadvisor.service.FlightService;
import com.github.nemojmenervirat.flightadvisor.utils.DistanceUtils;

@RestController
public class FlightController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private FlightService flightService;
	@Autowired
	private CityService cityService;
	@Autowired
	private FlightControllerCache cache;

	@GetMapping(UrlConstants.FLIGHT_CHEAPEST)
	public ResponseEntity<FlightResponse> getCheapest(@RequestParam String sourceCountry, @RequestParam String sourceCity,
			@RequestParam String destinationCountry, @RequestParam String destinationCity) {

		City source = cityService.getByCountryAndName(sourceCountry, sourceCity);
		City destination = cityService.getByCountryAndName(destinationCountry, destinationCity);

		FlightResponse cachedResponse = cache.get(source.getCityId(), destination.getCityId());
		if (cachedResponse != null) {
			log.info("Returning from cache.");
			return ResponseEntity.ok(cachedResponse);
		}
		List<Route> routes = flightService.getCheapestRoute(source, destination);
		if (routes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		FlightResponse flightResponse = map(routes);

		cache.add(source.getCityId(), destination.getCityId(), flightResponse);
		return ResponseEntity.ok(flightResponse);
	}

	private FlightResponse map(List<Route> routes) {
		FlightResponse flightResponse = new FlightResponse();
		flightResponse.setRoutes(new LinkedList<>());
		flightResponse.setPrice(BigDecimal.ZERO);
		flightResponse.setDistance(BigDecimal.ZERO);
		flightResponse.setDuration(BigDecimal.ZERO);
		for (Route route : routes) {
			RouteResponse routeResponse = new RouteResponse();
			routeResponse.setAirline(route.getAirline());
			routeResponse.setSourceCity(route.getSourceCity().toString());
			routeResponse.setDestinationCity(route.getDestinationCity().toString());
			routeResponse.setPrice(route.getPrice());
			routeResponse.setDistance(DistanceUtils.calclulateDistanceBetweenTwoAirports(route.getSourceAirport(), route.getDestinationAirport()));
			routeResponse.setDuration(DistanceUtils.calculateDuration(routeResponse.getDistance()));

			flightResponse.getRoutes().add(routeResponse);
			flightResponse.setPrice(flightResponse.getPrice().add(routeResponse.getPrice()));
			flightResponse.setDistance(flightResponse.getDistance().add(routeResponse.getDistance()));
			flightResponse.setDuration(flightResponse.getDuration().add(routeResponse.getDuration()));
		}
		return flightResponse;
	}
}
