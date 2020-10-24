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
import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.repository.AirportRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.UnescapedQuoteHandling;

@Service
public class AirportService {

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private AirportRepository airportRepository;

	public List<Airport> findAll() {
		return airportRepository.findAll();
	}

	public int upload(InputStream inputStream) throws IOException {
		List<City> cities = cityRepository.findAll();
		List<Airport> airports = parse(inputStream, cities);
		airportRepository.saveAll(airports);
		return airports.size();
	}

	public List<Airport> parse(InputStream inputStream, List<City> cities) throws IOException {
		Map<String, City> cityMap = cityRepository.findAll().stream()
				.collect(Collectors.toMap(city -> cityCountryKey(city.getCountry(), city.getName()), city -> city));
		List<Airport> airports = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.setUnescapedQuoteHandling(UnescapedQuoteHandling.STOP_AT_CLOSING_QUOTE);
		settings.setProcessor(new Processor<Context>() {

			@Override
			public void processStarted(Context context) {

			}

			@Override
			public void rowProcessed(String[] row, Context context) {
				String cityName = row[2];
				String countryName = row[3];
				String key = cityCountryKey(countryName, cityName);
				City city = cityMap.get(key);
				if (city == null) {
					return;
				}

				Airport airport = new Airport();
				airport.setAirportId(Long.parseLong(row[0]));
				airport.setName(row[1]);
				airport.setCity(city);
				airport.setIata(row[4]);
				airport.setIcao(row[5]);
				airport.setLatitude(new BigDecimal(row[6]));
				airport.setLongitude(new BigDecimal(row[7]));
				airports.add(airport);
			}

			@Override
			public void processEnded(Context context) {

			}
		});

		CsvParser parser = new CsvParser(settings);
		parser.parse(inputStream);
		return airports;
	}

	private String cityCountryKey(String country, String city) {
		return country + "_" + city;
	}

}
