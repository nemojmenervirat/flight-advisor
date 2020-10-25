package com.github.nemojmenervirat.flightadvisor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.service.RouteService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@RestController
public class RouteController {

	@Autowired
	private RouteService routeService;

	@GetMapping(UrlConstants.ROUTES)
	public List<Route> get() {
		return routeService.findAll();
	}

	@PostMapping(UrlConstants.ROUTES_IMPORT)
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		if (!FileUtils.isTxtOrCsv(file)) {
			throw new CustomException("Please upload .txt or .csv file!");
		}
		try {
			int count = routeService.upload(file.getInputStream());
			return ResponseEntity.ok("Successfully imported " + count + " routes.");
		} catch (IOException ex) {
			throw new CustomException("I/O Exception: " + ex.getMessage());
		}
	}
}
