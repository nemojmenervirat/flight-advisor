package com.github.nemojmenervirat.flightadvisor.parsecsv;

import java.util.Map;

import com.github.nemojmenervirat.flightadvisor.model.Airport;
import com.github.nemojmenervirat.flightadvisor.model.Route;

public class ParseRoutesContext extends ParseItemsContext<Route> {

	private Map<Long, Airport> airportIdMap;
	private Map<String, Airport> airportIataMap;
	private Map<String, Airport> airportIcaoMap;
	private Map<String, Route> existingRouteMap;

	public Map<String, Route> getExistingRouteMap() {
		return existingRouteMap;
	}

	public void setExistingRouteMap(Map<String, Route> existingRouteMap) {
		this.existingRouteMap = existingRouteMap;
	}

	public Map<Long, Airport> getAirportIdMap() {
		return airportIdMap;
	}

	public void setAirportIdMap(Map<Long, Airport> airportIdMap) {
		this.airportIdMap = airportIdMap;
	}

	public Map<String, Airport> getAirportIataMap() {
		return airportIataMap;
	}

	public void setAirportIataMap(Map<String, Airport> airportIataMap) {
		this.airportIataMap = airportIataMap;
	}

	public Map<String, Airport> getAirportIcaoMap() {
		return airportIcaoMap;
	}

	public void setAirportIcaoMap(Map<String, Airport> airportIcaoMap) {
		this.airportIcaoMap = airportIcaoMap;
	}

}
