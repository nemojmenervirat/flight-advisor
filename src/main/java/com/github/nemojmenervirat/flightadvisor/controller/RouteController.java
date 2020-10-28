package com.github.nemojmenervirat.flightadvisor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsResult;
import com.github.nemojmenervirat.flightadvisor.service.RouteService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@RestController
public class RouteController {

	@Autowired
	private RouteService routeService;

	@PostMapping(UrlConstants.ROUTES_IMPORT)
	public String upload(@RequestParam("file") MultipartFile file) {
		if (!FileUtils.isTxtOrCsv(file)) {
			throw new CustomException("Please upload .txt or .csv file!");
		}
		ParseItemsResult result = FileUtils.parseCsv(file, routeService);
		return "Successfully imported " + result.getImported() + " routes. Ignored " + result.getIgnored() + " rows.";

	}
}
