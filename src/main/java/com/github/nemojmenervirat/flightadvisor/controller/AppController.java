package com.github.nemojmenervirat.flightadvisor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String index() {
		//@formatter:off
		return "<html>"
				+ "<header>"
					+ "<title>Flight Advisor</title>"
				+ "</header>"
				+ "<body>Detailed information about API can be found "
					+ "<a href='https://github.com/nemojmenervirat/flight-advisor'>here.</a>" + 
				" </body>"
			+ "</html>";
		//@formatter:on
	}

	@PostMapping("/reset")
	public void reset() {
		routeRepository.deleteAll();
		airportRepository.deleteAll();
		commentRepository.deleteAll();
		cityRepository.deleteAll();
		appUserRepository.deleteAll();
	}

}
