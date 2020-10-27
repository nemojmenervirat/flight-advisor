package com.github.nemojmenervirat.flightadvisor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsResult;
import com.github.nemojmenervirat.flightadvisor.service.AirportService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@RestController
public class AirportController {

	@Autowired
	private AirportService airportService;

	@GetMapping(UrlConstants.AIRPORTS)
	public List<Airport> get() {
		return airportService.getAll();
	}

	@PostMapping(UrlConstants.AIRPORTS_IMPORT)
	public String upload(@RequestParam("file") MultipartFile file) {
		if (!FileUtils.isTxtOrCsv(file)) {
			throw new CustomException("Please upload .txt or .csv file!");
		}
		ParseItemsResult result = FileUtils.parseCsv(file, airportService);
		return "Successfully imported " + result.getImported() + " airports. Ignored " + result.getIgnored() + " rows.";
	}

}
