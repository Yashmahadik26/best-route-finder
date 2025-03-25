package main.java.com.bestroute.service.traveltimecalculator;

import main.java.com.bestroute.model.Location;

import java.util.*;

/**
 * Implementation of TravelTimeCalculator that computes travel time using the haversine formula.
 * This implementation precomputes the travel time between all unique locations.
 */
public class HaversineTravelTimeCalculatorImpl implements TravelTimeCalculator {

    private final double averageSpeed; // in km/hr
    private Map<Location, Integer> locationToIndex;
    private double[][] travelTimes; // travelTimes[i][j] holds precomputed travel time (in minutes)

    public HaversineTravelTimeCalculatorImpl(double averageSpeed, Set<Location> locations) {
        this.averageSpeed = averageSpeed;
        precomputeTravelTimes(locations);
    }

    private void precomputeTravelTimes(Set<Location> locations) {
        List<Location> locationList = new ArrayList<>(locations);
        locationToIndex = new HashMap<>();
        for (int i = 0; i < locationList.size(); i++) {
            locationToIndex.put(locationList.get(i), i);
        }
        int size = locationList.size();
        travelTimes = new double[size][size];
        // Since we are using geolocations, there would be a straight path between them
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                travelTimes[i][j] = computeHaversine(locationList.get(i), locationList.get(j));
            }
        }
    }

    // From Google
    private double computeHaversine(Location from, Location to) {
        double lat1 = from.getLatitude(), lon1 = from.getLongitude();
        double lat2 = to.getLatitude(), lon2 = to.getLongitude();
        final double R = 6371.0; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceKm = R * c;
        return (distanceKm / averageSpeed) * 60.0;
    }

    @Override
    public double calculateTravelTime(Location from, Location to) {
        return computeHaversine(from, to);
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
