package com.github.nemojmenervirat.flightadvisor.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.repository.CityRepository;
import com.github.nemojmenervirat.flightadvisor.repository.RouteRepository;

@Service
public class FlightService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private CityRepository cityRepository;

	public List<Route> getCheapestRoute(City sourceCity, City destinationCity) {
		log.info("Finding cheapest route");
		List<City> cities = cityRepository.findAll();
		List<Route> routes = routeRepository.findAllByOrderBySourceCityAscDestinationCityAsc();
		List<Node> nodes = createNodes(cities, routes);
		Node sourceNode = nodeMap.get(sourceCity);
		calculateShortestPath(nodes, sourceNode);
		Node destinationNode = nodeMap.get(destinationCity);
		return destinationNode.getShortestPath();
	}

	List<Node> nodes = new LinkedList<>();
	Map<City, Node> nodeMap = new HashMap<>();

	private List<Node> createNodes(List<City> cities, List<Route> routes) {
		nodes.clear();
		nodeMap.clear();
		for (City city : cities) {
			Node node = new Node(city);
			nodes.add(node);
			nodeMap.put(city, node);
		}
		Node sourceNode = null;
		Node destinationNode = null;
		for (Route route : routes) {
			if (sourceNode != null && sourceNode.getCity().equals(route.getSourceCity()) && destinationNode != null
					&& destinationNode.getCity().equals(route.getDestinationCity())) {
				Route currentRoute = sourceNode.getAdjacentNodes().get(destinationNode);
				if (compare(currentRoute, route) > 0) {
					sourceNode.addDestination(destinationNode, route);
				}
			} else {
				sourceNode = nodeMap.get(route.getSourceCity());
				destinationNode = nodeMap.get(route.getDestinationCity());
				sourceNode.addDestination(destinationNode, route);
			}
		}
		return nodes;
	}

	class Node {
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

	private int compare(Route r1, Route r2) {
		return r1.getPrice().compareTo(r2.getPrice());
	}

	private Integer getDistance(Route route) {
		return route.getPrice().multiply(new BigDecimal(100)).intValue();
	}

	private void calculateShortestPath(List<Node> nodes, Node source) {
		source.setDistance(0);

		Set<Node> settledNodes = new HashSet<>();
		Set<Node> unsettledNodes = new HashSet<>();

		unsettledNodes.add(source);

		while (unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
			for (Entry<Node, Route> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				if (!settledNodes.contains(adjacentNode)) {
					calculateMinimumDistance(adjacentNode, adjacencyPair.getValue(), currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			settledNodes.add(currentNode);
		}
	}

	private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		Integer lowestDistance = Integer.MAX_VALUE;
		for (Node node : unsettledNodes) {
			Integer nodeDistance = node.getDistance();
			if (nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}

	private void calculateMinimumDistance(Node evaluationNode, Route route, Node sourceNode) {
		Integer edgeWeight = getDistance(route);
		if (sourceNode.getDistance() + edgeWeight < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceNode.getDistance() + edgeWeight);
			LinkedList<Route> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
			shortestPath.add(route);
			evaluationNode.setShortestPath(shortestPath);
		}
	}
}
