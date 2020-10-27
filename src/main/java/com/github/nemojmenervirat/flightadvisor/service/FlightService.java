package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;

public interface FlightService {

	public List<Route> getCheapestRoute(City source, City destination);

}
