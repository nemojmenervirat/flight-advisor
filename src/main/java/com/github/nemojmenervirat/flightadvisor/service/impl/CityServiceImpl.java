package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.exception.AuthorizationException;
import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Comment;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseCitiesContext;
import com.github.nemojmenervirat.flightadvisor.payload.CityRequest;
import com.github.nemojmenervirat.flightadvisor.payload.CityResponse;
import com.github.nemojmenervirat.flightadvisor.payload.CommentResponse;
import com.github.nemojmenervirat.flightadvisor.repository.AppUserRepository;
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
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public List<CityResponse> getByNameLimitComments(String name, Integer limitComments) {
		List<City> cities = name == null ? cityRepository.findAll() : cityRepository.findAllByNameContainsIgnoreCase(name);
		for (City city : cities) {
			city.setComments(limitComments == null ? commentRepository.findAllByCityOrderByModifiedDesc(city)
					: commentRepository.findAllByCityOrderByModifiedDesc(city, limitComments));
		}
		return cities.stream().map(city -> map(city)).collect(Collectors.toList());
	}

	private CityResponse map(City city) {
		CityResponse cityResponse = new CityResponse();
		cityResponse.setCityId(city.getCityId());
		cityResponse.setName(city.getName());
		cityResponse.setCountry(city.getCountry());
		cityResponse.setDescription(city.getDescription());
		cityResponse.setComments(city.getComments().stream().map(comment -> map(comment)).collect(Collectors.toList()));
		return cityResponse;
	}

	private CommentResponse map(Comment comment) {
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.setCommentId(comment.getCommentId());
		commentResponse.setDescription(comment.getDescription());
		commentResponse.setModified(comment.getModified());
		return commentResponse;
	}

	@Override
	public void add(CityRequest request) {
		try {
			City city = new City();
			city.setName(request.getName());
			city.setCountry(request.getCountry());
			city.setDescription(request.getDescription());
			cityRepository.save(city);
		} catch (Exception ex) {
			if (ex.getMessage().contains("CITY(NAME, COUNTRY)")) {
				throw new CustomException("City " + request + " already exsists!");
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
		comment.setAppUser(appUserRepository.findOneByUsernameIgnoreCase(SecurityUtils.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + SecurityUtils.getUsername())));
		commentRepository.save(comment);
	}

	@Override
	public void updateComment(Long commentId, String description) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!SecurityUtils.isCurrent(comment.getAppUser())) {
			throw new AuthorizationException("You can update only comments you add!");
		}
		comment.setModified(LocalDateTime.now());
		comment.setDescription(description);
		commentRepository.save(comment);
	}

	@Override
	public void removeComment(Long commentId) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!SecurityUtils.isCurrent(comment.getAppUser())) {
			throw new AuthorizationException("You can delete only comments you add!");
		}
		commentRepository.delete(comment);
	}

	@Override
	public ParseCitiesContext processStarted() {
		ParseCitiesContext context = new ParseCitiesContext();
		Map<String, City> existingCityMap = cityRepository.findAll().stream().collect(Collectors.toMap(city -> city.getKey(), city -> city));
		context.setExistingCityMap(existingCityMap);
		return context;
	}

	@Override
	public void rowProcessed(String[] row, ParseCitiesContext context) {
		City city = new City();
		city.setName(row[2]);
		city.setCountry(row[3]);
		if (city.getName() == null || city.getName().isBlank() || city.getCountry() == null || city.getCountry().isBlank()
				|| context.getExistingCityMap().containsKey(city.getKey())) {
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
