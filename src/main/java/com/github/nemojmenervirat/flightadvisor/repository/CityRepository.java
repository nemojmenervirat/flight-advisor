package com.github.nemojmenervirat.flightadvisor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findAllByNameContainsIgnoreCase(String name);

	default City findByIdOrThrow(Long cityId) {
		return findById(cityId).orElseThrow(() -> new CustomException("City with id " + cityId + " not found!"));
	}

}
