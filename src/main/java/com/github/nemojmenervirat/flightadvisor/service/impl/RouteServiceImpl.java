package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseRoutesContext;
import com.github.nemojmenervirat.flightadvisor.repository.AirportRepository;
import com.github.nemojmenervirat.flightadvisor.repository.RouteRepository;
import com.github.nemojmenervirat.flightadvisor.service.RouteService;

@Service
class RouteServiceImpl implements RouteService {

	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private RouteRepository routeRepository;

	@Override
	public List<Route> getAll() {
		return routeRepository.findAll();
	}

	private Long tryParse(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private Airport findAirport(String airportIdString, String airportCode, ParseRoutesContext context) {
		Long airportId = tryParse(airportIdString);
		if (airportId != null) {
			return context.getAirportIdMap().get(airportId);
		}
		if (airportCode != null) {
			if (airportCode.length() == 3) {
				return context.getAirportIataMap().get(airportCode);
			}
			if (airportCode.length() == 4) {
				return context.getAirportIcaoMap().get(airportCode);
			}
		}
		return null;
	}

	@Override
	public ParseRoutesContext processStarted() {
		ParseRoutesContext context = new ParseRoutesContext();
		List<Airport> airports = airportRepository.findAll();
		Map<Long, Airport> airportIdMap = airports.stream().collect(Collectors.toMap(airport -> airport.getAirportId(), airport -> airport));
		Map<String, Airport> airportIataMap = airports.stream().filter(airport -> airport.getIata() != null && !airport.getIata().equals("\\N"))
				.collect(Collectors.toMap(airport -> airport.getIata(), airport -> airport));
		Map<String, Airport> airportIcaoMap = airports.stream().filter(airport -> airport.getIcao() != null && !airport.getIcao().equals("\\N"))
				.collect(Collectors.toMap(airport -> airport.getIcao(), airport -> airport));
		context.setAirportIdMap(airportIdMap);
		context.setAirportIataMap(airportIataMap);
		context.setAirportIcaoMap(airportIcaoMap);
		return context;
	}

	@Override
	public void rowProcessed(String[] row, ParseRoutesContext context) {

		Airport sourceAirport = findAirport(row[3], row[2], context);
		if (sourceAirport == null) {
			return;
		}

		Airport destinationAirport = findAirport(row[5], row[4], context);
		if (destinationAirport == null) {
			context.ignoreRow();
			return;
		}

		Route route = new Route();
		route.setAirline(row[0]);
		route.setAirlineId(tryParse(row[1]));
		route.setSourceAirport(sourceAirport);
		route.setDestinationAirport(destinationAirport);
		route.setSourceCity(sourceAirport.getCity());
		route.setDestinationCity(destinationAirport.getCity());
		route.setPrice(new BigDecimal(row[9]));
		context.addResult(route);
	}

	@Override
	public void processEnded(ParseRoutesContext context) {
		routeRepository.saveAll(context.getResultList());
	}
}
