package com.github.nemojmenervirat.flightadvisor.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Comment;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.repository.CommentRepository;
import com.github.nemojmenervirat.flightadvisor.security.SecurityUtils;
import com.github.nemojmenervirat.flightadvisor.service.CityService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@RestController
public class CityController {

	@Autowired
	private CityService cityService;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CommentRepository commentRepository;

	@GetMapping(UrlConstants.CITIES)
	public List<City> get(@RequestParam(required = false) Integer limitComments, @RequestParam(required = false) String name) {
		List<City> cities = name == null ? cityRepository.findAll() : cityRepository.findAllByNameContainsIgnoreCase(name);
		for (City city : cities) {
			city.setComments(limitComments == null ? commentRepository.findAllByCityOrderByModifiedDesc(city)
					: commentRepository.findAllByCityOrderByModifiedDesc(city, limitComments));
		}
		return cities;
	}

	@PostMapping(UrlConstants.CITIES)
	public void add(@RequestBody @Valid City city) {
		try {
			cityRepository.save(city);
		} catch (Exception ex) {
			if (ex.getMessage().contains("CITY(NAME, COUNTRY)")) {
				throw new CustomException("City " + city + " already exsists!");
			}
			throw new CustomException(ex.getMessage());
		}
	}

	@PostMapping(UrlConstants.CITIES_IMPORT)
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		if (!FileUtils.isTxtOrCsv(file)) {
			throw new CustomException("Please upload .txt or .csv file!");
		}
		try {
			int count = cityService.upload(file.getInputStream());
			return ResponseEntity.ok("Successfully imported " + count + " cities.");
		} catch (IOException ex) {
			throw new CustomException("I/O Exception: " + ex.getMessage());
		}
	}

	@DeleteMapping(UrlConstants.CITY)
	public void delete(@PathVariable Long cityId) {
		cityRepository.deleteById(cityId);
	}

	@PostMapping(UrlConstants.CITY_COMMENTS)
	public void addComment(@PathVariable Long cityId, @RequestBody String description) {
		City city = cityRepository.findByIdOrThrow(cityId);
		Comment comment = new Comment();
		comment.setDescription(description);
		comment.setModified(LocalDateTime.now());
		comment.setCreated(comment.getModified());
		comment.setCity(city);
		comment.setAppUser(SecurityUtils.getAppUser());
		commentRepository.save(comment);
	}

	@PutMapping(UrlConstants.CITY_COMMENT)
	public void updateComment(@PathVariable Long cityId, @PathVariable Long commentId, @RequestBody String description) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!comment.getAppUser().equals(SecurityUtils.getAppUser())) {
			throw new CustomException("You can update only comments you add!");
		}
		comment.setModified(LocalDateTime.now());
		comment.setDescription(description);
		commentRepository.save(comment);
	}

	@DeleteMapping(UrlConstants.CITY_COMMENT)
	public void deleteComment(@PathVariable Long cityId, @PathVariable Long commentId) {
		Comment comment = commentRepository.findByIdOrThrow(commentId);
		if (!comment.getAppUser().equals(SecurityUtils.getAppUser())) {
			throw new CustomException("You can delete only comments you add!");
		}
		commentRepository.delete(comment);
	}

}
