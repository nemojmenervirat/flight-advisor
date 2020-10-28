package com.github.nemojmenervirat.flightadvisor.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;

public class Node {

	private City city;
	private List<Route> shortestPath = new LinkedList<>();
	private Integer distance = Integer.MAX_VALUE;
	private Map<Node, Route> adjacentNodes = new HashMap<>();

	public void addDestination(Node destination, Route route) {
		adjacentNodes.put(destination, route);
	}

	public Node(City city) {
		this.city = city;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<Route> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Route> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Map<Node, Route> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Route> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}
}
