package main.java.com.bestroute.service.traveltimecalculator;

import main.java.com.bestroute.model.Location;

/**
 * A strategy interface for computing travel time (in minutes) between two locations.
 */
public interface TravelTimeCalculator {
    double calculateTravelTime(Location from, Location to);

    double getTravelTime(Location from, Location to);
}
