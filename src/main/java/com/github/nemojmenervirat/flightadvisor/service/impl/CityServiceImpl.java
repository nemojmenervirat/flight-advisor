package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Comment;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseCitiesContext;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CommentRepository;
import com.github.nemojmenervirat.flightadvisor.security.SecurityUtils;
import com.github.nemojmenervirat.flightadvisor.service.CityService;

@Service
class CityServiceImpl implements CityService {

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<City> getByNameLimitComments(String name, Integer limitComments) {
		List<City> cities = name == null ? cityRepository.findAll() : cityRepository.findAllByNameContainsIgnoreCase(name);
		for (City city : cities) {
			city.setComments(limitComments == null ? commentRepository.findAllByCityOrderByModifiedDesc(city)
					: commentRepository.findAllByCityOrderByModifiedDesc(city, limitComments));
		}
		return cities;
	}

	@Override
	public void add(City city) {
		try {
			cityRepository.save(city);
		} catch (Exception ex) {
			if (ex.getMessage().contains("CITY(NAME, COUNTRY)")) {
				throw new CustomException("City " + city + " already exsists!");
			}
			throw new CustomException(ex.getMessage());
		}
	}

	@Override
	public void remove(Long cityId) {
		cityRepository.deleteById(cityId);
	}

	@Override
	public void addComment(Long cityId, String description) {
		City city = cityRepository.findByIdOrThrow(cityId);
		Comment comment = new Comment();
		comment.setDescription(description);
		comment.setModified(LocalDateTime.now());
		comment.setCreated(comment.getModified());
		comment.setCity(city);
		comment.setAppUser(SecurityUtils.getAppUser());
		commentRepository.save(comment);
	}

	@Override
	public void updateComment(Long commentId, String description) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!comment.getAppUser().equals(SecurityUtils.getAppUser())) {
			throw new CustomException("You can update only comments you add!");
		}
		comment.setModified(LocalDateTime.now());
		comment.setDescription(description);
		commentRepository.save(comment);
	}

	@Override
	public void removeComment(Long commentId) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!comment.getAppUser().equals(SecurityUtils.getAppUser())) {
			throw new CustomException("You can delete only comments you add!");
		}
		commentRepository.delete(comment);
	}

	@Override
	public City getByCountryAndName(String country, String name) {
		return cityRepository.findByCountryAndNameOrThrow(country, name);
	}

	@Override
	public ParseCitiesContext processStarted() {
		return new ParseCitiesContext();
	}

	@Override
	public void rowProcessed(String[] row, ParseCitiesContext context) {
		City city = new City();
		city.setName(row[2]);
		city.setCountry(row[3]);
		if (city.getName() == null || city.getName().isBlank() || city.getCountry() == null || city.getCountry().isBlank()) {
			context.ignoreRow();
			return;
		}
		city.setDescription("Import");
		if (!context.getResultList().contains(city)) {
			context.addResult(city);
		}
	}

	@Override
	public void processEnded(ParseCitiesContext context) {
		cityRepository.saveAll(context.getResultList());
	}

}
