package com.github.nemojmenervirat.flightadvisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.model.Airport;

public interface AirportRepository extends JpaRepository<Airport, Long> {

}
