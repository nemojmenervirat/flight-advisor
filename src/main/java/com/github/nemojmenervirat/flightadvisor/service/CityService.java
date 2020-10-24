package com.github.nemojmenervirat.flightadvisor.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.UnescapedQuoteHandling;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	public int upload(InputStream inputStream) throws IOException {
		List<City> cities = parse(inputStream);
		cityRepository.saveAll(cities);
		return cities.size();
	}

	public List<City> parse(InputStream inputStream) throws IOException {
		List<City> cities = new LinkedList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.setUnescapedQuoteHandling(UnescapedQuoteHandling.STOP_AT_CLOSING_QUOTE);
		settings.setProcessor(new Processor<Context>() {

			@Override
			public void processStarted(Context context) {

			}

			@Override
			public void rowProcessed(String[] row, Context context) {
				City city = new City();
				city.setName(row[2]);
				city.setCountry(row[3]);
				if (city.getName() == null || city.getName().isBlank() || city.getCountry() == null || city.getCountry().isBlank()) {
					return;
				}
				city.setDescription("Import");
				if (!cities.contains(city)) {
					cities.add(city);
				}
			}

			@Override
			public void processEnded(Context context) {

			}
		});

		CsvParser parser = new CsvParser(settings);
		parser.parse(inputStream);
		return cities;
	}

}
