package com.github.nemojmenervirat.flightadvisor.parsecsv;

import java.util.Map;

import com.github.nemojmenervirat.flightadvisor.model.City;

public class ParseCitiesContext extends ParseItemsContext<City> {

	private Map<String, City> existingCityMap;

	public Map<String, City> getExistingCityMap() {
		return existingCityMap;
	}

	public void setExistingCityMap(Map<String, City> existingCityMap) {
		this.existingCityMap = existingCityMap;
	}

}