package com.github.nemojmenervirat.flightadvisor.parsecsv;

import java.util.Map;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.model.City;

public class ParseAirportsContext extends ParseItemsContext<Airport> {

	private Map<String, City> cityMap;
	private Map<Long, Airport> existingAirportMap;

	public Map<Long, Airport> getExistingAirportMap() {
		return existingAirportMap;
	}

	public void setExistingAirportMap(Map<Long, Airport> existingAirportMap) {
		this.existingAirportMap = existingAirportMap;
	}

	public Map<String, City> getCityMap() {
		return cityMap;
	}

	public void setCityMap(Map<String, City> cityMap) {
		this.cityMap = cityMap;
	}

}