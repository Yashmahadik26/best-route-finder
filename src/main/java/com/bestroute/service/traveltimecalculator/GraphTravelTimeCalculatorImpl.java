package main.java.com.bestroute.service.traveltimecalculator;

import main.java.com.bestroute.model.Location;

import java.util.*;

/**
 * Implementation of TravelTimeCalculator that uses a provided adjacency list.
 * The input graph is given as a mapping from a Location (source) to a map of destination Locations and travel times.
 * This implementation precomputes travel times for all unique locations.
 * If a path from A to B is not provided, then travel from A to B is considered unavailable (infinite time).
 * Provided a separate strategy as this is more real-life calculation
 */
public class GraphTravelTimeCalculatorImpl implements TravelTimeCalculator {

    private final Map<Location, Map<Location, Double>> graph;
    private Map<Location, Integer> locationToIndex;
    private double[][] travelTimes;

    public GraphTravelTimeCalculatorImpl(Map<Location, Map<Location, Double>> graph) {
        this.graph = graph;
        precomputeTravelTimes();
    }

    private void precomputeTravelTimes() {
        // Collect unique locations from both keys and their destination maps.
        Set<Location> locSet = new HashSet<>();
        for (Location from : graph.keySet()) {
            locSet.add(from);
            locSet.addAll(graph.get(from).keySet());
        }
        List<Location> locations = new ArrayList<>(locSet);
        locationToIndex = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            locationToIndex.put(locations.get(i), i);
        }
        int size = locations.size();
        travelTimes = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location from = locations.get(i);
                Location to = locations.get(j);
                travelTimes[i][j] = calculateTravelTime(from, to);
            }
        }
    }

    @Override
    public double calculateTravelTime(Location from, Location to) {
        double time = Double.POSITIVE_INFINITY;;
        if (graph.containsKey(from) && graph.get(from).containsKey(to)) {
            time = graph.get(from).get(to);
        }
        return time;
    }

    @Override
    public double getTravelTime(Location from, Location to) {
        // directly checking in the 2d array as we pre-computed in constructor
        if (!locationToIndex.containsKey(from) || !locationToIndex.containsKey(to)) {
            return Double.POSITIVE_INFINITY;
        }
        int i = locationToIndex.get(from);
        int j = locationToIndex.get(to);
        return travelTimes[i][j];
    }
}
