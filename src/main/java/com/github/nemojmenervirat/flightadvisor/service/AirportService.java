package com.github.nemojmenervirat.flightadvisor.service;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseAirportsContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;

public interface AirportService extends ParseProcessor<Airport, ParseAirportsContext> {

}
