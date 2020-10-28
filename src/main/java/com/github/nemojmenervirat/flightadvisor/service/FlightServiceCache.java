package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;
import java.util.Map;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;
import com.github.nemojmenervirat.flightadvisor.utils.Graph;
import com.github.nemojmenervirat.flightadvisor.utils.Node;

public interface FlightServiceCache {

	public FlightResponse getFlightResponse(long sourceCityId, long destinationCityId);

	public void addFlightResponse(long sourceCityId, long destinationCityId, FlightResponse flightResponse);

	public Graph getGraph();

	public void createGraph(List<Node> nodes, Map<City, Node> cityNodeMap);

	public void clear();

}
