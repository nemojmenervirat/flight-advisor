package com.github.nemojmenervirat.flightadvisor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.nemojmenervirat.flightadvisor.repository.AirportRepository;
import com.github.nemojmenervirat.flightadvisor.repository.AppUserRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CommentRepository;
import com.github.nemojmenervirat.flightadvisor.repository.RouteRepository;

@RestController
public class AppController {

	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private AppUserRepository appUserRepository;

	@PostMapping("/reset")
	public void reset() {
		routeRepository.deleteAll();
		airportRepository.deleteAll();
		commentRepository.deleteAll();
		cityRepository.deleteAll();
		appUserRepository.deleteAll();
	}

}
