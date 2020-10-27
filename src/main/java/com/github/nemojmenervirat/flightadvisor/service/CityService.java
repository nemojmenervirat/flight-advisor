package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseCitiesContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;

public interface CityService extends ParseProcessor<City, ParseCitiesContext> {

	public List<City> getByNameLimitComments(String name, Integer limitComments);

	public City getByCountryAndName(String country, String name);

	public void add(City city);

	public void remove(Long cityId);

	public void addComment(Long cityId, String description);

	public void updateComment(Long cityId, String description);

	public void removeComment(Long commentId);

}
