package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.github.nemojmenervirat.flightadvisor.utils.Graph;
import com.github.nemojmenervirat.flightadvisor.utils.Node;

@Service
class FlightServiceImpl implements FlightService {

	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private FlightServiceCache cache;

	@Override
	public FlightResponse getCheapestRoute(Long sourceCityId, Long destinationCityId) {

		City sourceCity = cityRepository.findByIdOrThrow(sourceCityId);
		City destinationCity = cityRepository.findByIdOrThrow(destinationCityId);

		FlightResponse cachedResponse = cache.getFlightResponse(sourceCity.getCityId(), destinationCity.getCityId());
		if (cachedResponse != null) {
			return cachedResponse;
		}

		List<City> cities = cityRepository.findAll();
		List<Route> routes = routeRepository.findAllByOrderBySourceAirport_CityAscDestinationAirport_CityAsc();
		Graph graph = createGraph(cities, routes);

		Node sourceNode = graph.get(sourceCity);
		if (sourceNode == null) {
			return null;
		}

		graph.calculateShortestPath(sourceNode);

		Node destinationNode = graph.get(destinationCity);
		if (destinationNode == null) {
			return null;
		}
		if (destinationNode.getShortestPath().isEmpty()) {
			return null;
		}
		FlightResponse flightResponse = map(destinationNode.getShortestPath());

		cache.addFlightResponse(sourceCity.getCityId(), destinationCity.getCityId(), flightResponse);

		return flightResponse;
	}

	private int compare(Route r1, Route r2) {
		return r1.getPrice().compareTo(r2.getPrice());
	}

	private Graph createGraph(List<City> cities, List<Route> routes) {
		Map<City, Node> cityNodeMap = new HashMap<>();
		for (City city : cities) {
			Node node = new Node(city);
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
		return new Graph(cityNodeMap);
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
