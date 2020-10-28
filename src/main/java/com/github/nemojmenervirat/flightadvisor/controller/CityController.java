package com.github.nemojmenervirat.flightadvisor.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsResult;
import com.github.nemojmenervirat.flightadvisor.payload.CityRequest;
import com.github.nemojmenervirat.flightadvisor.payload.CityResponse;
import com.github.nemojmenervirat.flightadvisor.payload.CommentRequest;
import com.github.nemojmenervirat.flightadvisor.service.CityService;
import com.github.nemojmenervirat.flightadvisor.utils.FileUtils;

@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	@GetMapping(UrlConstants.CITIES)
	public List<CityResponse> get(@RequestParam(required = false) String name, @RequestParam(required = false) Integer limitComments) {
		if (limitComments != null && limitComments < 1) {
			throw new CustomException("LimitComments must be greater than or equals to 1");
		}
		return cityService.getByNameLimitComments(name, limitComments);
	}

	@PostMapping(UrlConstants.CITIES)
	public void add(@RequestBody @Valid CityRequest request) {
		cityService.add(request);
	}

	@PostMapping(UrlConstants.CITIES_IMPORT)
	public String upload(@RequestParam("file") MultipartFile file) {
		if (!FileUtils.isTxtOrCsv(file)) {
			throw new CustomException("Please upload .txt or .csv file!");
		}
		ParseItemsResult result = FileUtils.parseCsv(file, cityService);
		return "Successfully imported " + result.getImported() + " cities. Ignored " + result.getIgnored() + " rows.";
	}

	@DeleteMapping(UrlConstants.CITY)
	public void delete(@PathVariable Long cityId) {
		cityService.remove(cityId);
	}

	@PostMapping(UrlConstants.CITY_COMMENTS)
	public void addComment(@PathVariable Long cityId, @RequestBody CommentRequest request) {
		cityService.addComment(cityId, request.getDescription());
	}

	@PutMapping(UrlConstants.CITY_COMMENT)
	public void updateComment(@PathVariable Long cityId, @PathVariable Long commentId, @RequestBody CommentRequest request) {
		cityService.updateComment(commentId, request.getDescription());
	}

	@DeleteMapping(UrlConstants.CITY_COMMENT)
	public void deleteComment(@PathVariable Long commentId) {
		cityService.removeComment(commentId);
	}

}
