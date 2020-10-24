package com.github.nemojmenervirat.flightadvisor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findAllByNameContainsIgnoreCase(String name);

	Optional<City> findByCountryAndName(String sourceCountry, String sourceCity);

}
