package com.github.nemojmenervirat.flightadvisor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseAirportsContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsResult;
import com.github.nemojmenervirat.flightadvisor.service.AirportService;
import com.github.nemojmenervirat.flightadvisor.service.CityService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@SpringBootTest
public class AirportServiceTests {

	@Autowired
	private AirportService airportService;
	@Autowired
	private CityService cityService;

	@Test
	void contextLoads() {
		assertThat(airportService).isNotNull();
	}

	@Test
	void parseRowForExsistingCity_expectImported() {
		ParseAirportsContext context = new ParseAirportsContext();
		Map<String, City> cityMap = new HashMap<>();
		City city = new City();
		city.setName("Goroka");
		city.setCountry("Papua New Guinea");
		cityMap.put(city.getCountry() + "_" + city.getName(), city);
		context.setCityMap(cityMap);

		String[] row = { "1", "Goroka Airport", "Goroka", "Papua New Guinea", "GKA", "AYGA", "-6.081689834590001", "145.391998291", "5282", "10",
				"U", "Pacific/Port_Moresby", "airport", "OurAirports" };
		airportService.rowProcessed(row, context);
		assertEquals(context.getImportedCount(), 1);
	}

	@Test
	void parseRowForNonExsistingCity_expectIgnored() {
		ParseAirportsContext context = new ParseAirportsContext();
		context.setCityMap(new HashMap<>());

		String[] row = { "2", "Madang Airport", "Madang", "Papua New Guinea", "MAG", "AYMD", "-5.20707988739", "145.789001465", "20", "10",
				"U", "Pacific/Port_Moresby", "airport", "OurAirports" };
		airportService.rowProcessed(row, context);
		assertEquals(context.getIgnoredCount(), 1);
		assertEquals(context.getImportedCount(), 0);
	}

	@Test
	@Rollback
	void parseAirportsDataset_expectSuccess() throws IOException {
		City city = new City();
		city.setName("New York");
		city.setCountry("United States");
		city.setDescription("Nice city");
		cityService.add(city);

		InputStream inputStream = new ClassPathResource("airports.txt").getInputStream();
		ParseItemsResult result = FileUtils.parseCsv(inputStream, airportService);
		assertEquals(result.getImported(), 4);
		assertEquals(result.getIgnored(), 7180);
	}

}
