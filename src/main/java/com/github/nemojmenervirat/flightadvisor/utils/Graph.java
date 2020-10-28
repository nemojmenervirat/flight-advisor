package com.github.nemojmenervirat.flightadvisor.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.github.nemojmenervirat.flightadvisor.model.City;
import com.github.nemojmenervirat.flightadvisor.model.Route;

public class Graph {

	private List<Node> nodes;;
	private Map<City, Node> cityNodeMap;;

	public Graph(List<Node> nodes, Map<City, Node> cityNodeMap) {
		this.nodes = Objects.requireNonNull(nodes);
		this.cityNodeMap = Objects.requireNonNull(cityNodeMap);
	}

	public void resetDistances() {
		nodes.forEach(node -> node.setDistance(Integer.MAX_VALUE));
	}

	public Node get(City city) {
		return cityNodeMap.get(city);
	}

	private static Integer getDistance(Route route) {
		return route.getPrice().multiply(new BigDecimal(100)).intValue();
	}

	public void calculateShortestPath(Node source) {
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
