package com.github.nemojmenervirat.flightadvisor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

	List<Route> findAllByOrderBySourceAirport_CityAscDestinationAirport_CityAsc();

}
