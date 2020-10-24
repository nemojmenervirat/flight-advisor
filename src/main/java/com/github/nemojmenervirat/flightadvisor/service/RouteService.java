package com.github.nemojmenervirat.flightadvisor.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.repository.AirportRepository;
import com.github.nemojmenervirat.flightadvisor.repository.RouteRepository;
import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.UnescapedQuoteHandling;

@Service
public class RouteService {

	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	private RouteRepository routeRepository;

	public List<Route> findAll() {
		return routeRepository.findAll();
	}

	public int upload(InputStream inputStream) throws IOException {
		List<Airport> airports = airportRepository.findAll();
		List<Route> routes = parse(inputStream, airports);
		routeRepository.saveAll(routes);
		return routes.size();
	}

	public List<Route> parse(InputStream inputStream, List<Airport> airports) throws IOException {
		Map<Long, Airport> airportIdMap = airports.stream().collect(Collectors.toMap(airport -> airport.getAirportId(), airport -> airport));
		Map<String, Airport> airportIataMap = airports.stream().filter(airport -> airport.getIata() != null && !airport.getIata().equals("\\N"))
				.collect(Collectors.toMap(airport -> airport.getIata(), airport -> airport));
		Map<String, Airport> airportIcaoMap = airports.stream().filter(airport -> airport.getIcao() != null && !airport.getIcao().equals("\\N"))
				.collect(Collectors.toMap(airport -> airport.getIcao(), airport -> airport));

		List<Route> routes = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.setUnescapedQuoteHandling(UnescapedQuoteHandling.STOP_AT_CLOSING_QUOTE);
		settings.setProcessor(new Processor<Context>() {

			@Override
			public void processStarted(Context context) {

			}

			@Override
			public void rowProcessed(String[] row, Context context) {
				Airport sourceAirport = findAirport(row[3], row[2], airportIdMap, airportIataMap, airportIcaoMap);
				if (sourceAirport == null) {
					return;
				}

				Airport destinationAirport = findAirport(row[5], row[4], airportIdMap, airportIataMap, airportIcaoMap);
				if (destinationAirport == null) {
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
				routes.add(route);
			}

			@Override
			public void processEnded(Context context) {

			}
		});

		CsvParser parser = new CsvParser(settings);
		parser.parse(inputStream);

		return routes;
	}

	private Long tryParse(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private Airport findAirport(String airportIdString, String airportCode, Map<Long, Airport> airportIdMap, Map<String, Airport> airportIataMap,
			Map<String, Airport> airportIcaoMap) {
		Long airportId = tryParse(airportIdString);
		if (airportId != null) {
			return airportIdMap.get(airportId);
		}
		if (airportCode != null) {
			if (airportCode.length() == 3) {
				return airportIataMap.get(airportCode);
			}
			if (airportCode.length() == 4) {
				return airportIcaoMap.get(airportCode);
			}
		}
		return null;
	}
}
