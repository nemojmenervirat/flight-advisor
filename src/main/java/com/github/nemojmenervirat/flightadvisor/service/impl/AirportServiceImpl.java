package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseAirportsContext;
import com.github.nemojmenervirat.flightadvisor.repository.AirportRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.service.AirportService;

@Service
class AirportServiceImpl implements AirportService {

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private AirportRepository airportRepository;

	private String cityCountryKey(String country, String city) {
		return country + "_" + city;
	}

	@Override
	public ParseAirportsContext processStarted() {
		ParseAirportsContext context = new ParseAirportsContext();
		Map<String, City> cityMap = cityRepository.findAll().stream()
				.collect(Collectors.toMap(city -> cityCountryKey(city.getCountry(), city.getName()), city -> city));
		context.setCityMap(cityMap);
		return context;
	}

	@Override
	public void rowProcessed(String[] row, ParseAirportsContext context) {
		String cityName = row[2];
		String countryName = row[3];
		String key = cityCountryKey(countryName, cityName);
		City city = context.getCityMap().get(key);
		if (city == null) {
			context.ignoreRow();
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
		context.addResult(airport);
	}

	@Override
	public void processEnded(ParseAirportsContext context) {
		airportRepository.saveAll(context.getResultList());
	}

}
