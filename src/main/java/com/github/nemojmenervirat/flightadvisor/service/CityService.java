package com.github.nemojmenervirat.flightadvisor.service;

import java.util.List;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseCitiesContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;
import com.github.nemojmenervirat.flightadvisor.payload.CityRequest;
import com.github.nemojmenervirat.flightadvisor.payload.CityResponse;

public interface CityService extends ParseProcessor<City, ParseCitiesContext> {

	public List<CityResponse> getByNameLimitComments(String name, Integer limitComments);

	public void add(CityRequest request);

	public void remove(Long cityId);

	public void addComment(Long cityId, String description);

	public void updateComment(Long cityId, String description);

	public void removeComment(Long commentId);

}
