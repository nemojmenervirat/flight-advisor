package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseAirportsContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;

public interface AirportService extends ParseProcessor<Airport, ParseAirportsContext> {

	public List<Airport> getAll();

}
