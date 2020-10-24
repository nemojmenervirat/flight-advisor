package com.github.nemojmenervirat.flightadvisor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findAllByNameContainsIgnoreCase(String name);

	Optional<City> findByCountryAndName(String sourceCountry, String sourceCity);

	default City findByIdOrThrow(Long cityId) {
		return findById(cityId).orElseThrow(() -> new CustomException("City with id " + cityId + " not found!"));
	}

	default City findByCountryAndNameOrThrow(String country, String name) {
		return findByCountryAndName(country, name).orElseThrow(() -> new CustomException("City " + name + " in " + country + " not found!"));
	}

}
