package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;
import com.github.nemojmenervirat.flightadvisor.payload.RouteResponse;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.repository.RouteRepository;
import com.github.nemojmenervirat.flightadvisor.service.FlightService;
import com.github.nemojmenervirat.flightadvisor.service.FlightServiceCache;
import com.github.nemojmenervirat.flightadvisor.utils.DistanceUtils;
import com.github.nemojmenervirat.flightadvisor.utils.Node;

@Service
class FlightServiceImpl implements FlightService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private FlightServiceCache cache;

	@Override
	public FlightResponse getCheapestRoute(String sourceCountry, String sourceCity, String destinationCountry, String destinationCity) {

		City source = cityRepository.findByCountryAndNameOrThrow(sourceCountry, sourceCity);
		City destination = cityRepository.findByCountryAndNameOrThrow(destinationCountry, destinationCity);

		FlightResponse cachedResponse = cache.getFlightResponse(source.getCityId(), destination.getCityId());
		if (cachedResponse != null) {
			log.info("Returning cached response.");
			return cachedResponse;
		}
		if (cache.getGraph() == null) {
			List<City> cities = cityRepository.findAll();
			List<Route> routes = routeRepository.findAllByOrderBySourceAirport_CityAscDestinationAirport_CityAsc();
			createNodes(cities, routes);
			log.info("Creating graph.");
		} else {
			cache.getGraph().resetDistances();
			log.info("Reseting distances.");
		}
		Node sourceNode = cache.getGraph().get(source);
		if (sourceNode == null) {
			return null;
		}
		cache.getGraph().calculateShortestPath(sourceNode);
		Node destinationNode = cache.getGraph().get(destination);
		if (destinationNode == null) {
			return null;
		}
		List<Route> routes = destinationNode.getShortestPath();

		if (routes.isEmpty()) {
			return null;
		}
		FlightResponse flightResponse = map(routes);
		cache.addFlightResponse(source.getCityId(), destination.getCityId(), flightResponse);
		return flightResponse;
	}

	private int compare(Route r1, Route r2) {
		return r1.getPrice().compareTo(r2.getPrice());
	}

	private void createNodes(List<City> cities, List<Route> routes) {
		List<Node> nodes = new LinkedList<>();
		Map<City, Node> cityNodeMap = new HashMap<>();
		for (City city : cities) {
			Node node = new Node(city);
			nodes.add(node);
			cityNodeMap.put(city, node);
		}
		Node sourceNode = null;
		Node destinationNode = null;
		for (Route route : routes) {
			if (sourceNode != null && sourceNode.getCity().equals(route.getSourceCity()) && destinationNode != null
					&& destinationNode.getCity().equals(route.getDestinationCity())) {
				Route currentRoute = sourceNode.getAdjacentNodes().get(destinationNode);
				if (compare(currentRoute, route) > 0) {
					sourceNode.addDestination(destinationNode, route);
				}
			} else {
				sourceNode = cityNodeMap.get(route.getSourceCity());
				destinationNode = cityNodeMap.get(route.getDestinationCity());
				sourceNode.addDestination(destinationNode, route);
			}
		}
		cache.createGraph(nodes, cityNodeMap);
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
