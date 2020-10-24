package com.github.nemojmenervirat.flightadvisor.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByCityOrderByModifiedDesc(City city);

	Page<Comment> findAllByCityOrderByModifiedDesc(City city, Pageable pageable);

	default List<Comment> findAllByCityOrderByModifiedDesc(City city, int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		return findAllByCityOrderByModifiedDesc(city, pageable).get().collect(Collectors.toList());
	}

}
