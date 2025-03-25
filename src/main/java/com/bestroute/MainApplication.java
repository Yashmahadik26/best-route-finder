package main.java.com.bestroute;

import main.java.com.bestroute.model.Location;
import main.java.com.bestroute.model.Order;
import main.java.com.bestroute.service.findbestroute.BestRouteFinder;
import main.java.com.bestroute.service.findbestroute.BestRouteFinderPQStrategy;
import main.java.com.bestroute.service.findbestroute.BestRouteResult;
import main.java.com.bestroute.service.traveltimecalculator.GraphTravelTimeCalculatorImpl;
import main.java.com.bestroute.service.traveltimecalculator.HaversineTravelTimeCalculatorImpl;
import main.java.com.bestroute.service.traveltimecalculator.TravelTimeCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainApplication {
    public static void main(String[] args) {
        // Define starting location.
        Location start = new Location("AmanStart", 12.9716, 77.5946);

        // Define example orders.
        // Order 1: From Restaurant R1 (prep time 15) to Customer C1.
        Location restaurant1 = new Location("R1", 12.9352, 77.6245);
        Location customer1 = new Location("C1", 12.9279, 77.6271);
        Order order1 = new Order(restaurant1, customer1, 15.0);

        // Order 2: From Restaurant R1 (prep time 20) to Customer C2.
        Location customer2 = new Location("C2", 12.9300, 77.6300);
        Order order2 = new Order(restaurant1, customer2, 20.0);

        // Order 3: From Restaurant R1 (prep time 25) to Customer C3.
        Location customer3 = new Location("C3", 12.9320, 77.6320);
        Order order3 = new Order(restaurant1, customer3, 25.0);

        // Order 4: From Restaurant R2 (prep time 18) to Customer C4.
        Location restaurant2 = new Location("R2", 12.9538, 77.4900);
        Location customer4 = new Location("C4", 12.9500, 77.5400);
        Order order4 = new Order(restaurant2, customer4, 18.0);

        Order[] orders = new Order[] { order1, order2, order3, order4 };

        // Option 1: Use Haversine-based calculator.
        Set<Location> uniqueLocations = new HashSet<>();
        uniqueLocations.add(start);
        for (Order o : orders) {
            uniqueLocations.add(o.getRestaurant());
            uniqueLocations.add(o.getCustomer());
        }
        TravelTimeCalculator haversineCalc = new HaversineTravelTimeCalculatorImpl(20.0, uniqueLocations);

        // You can switch between the calculators below.
        BestRouteFinder bestRouteFinder = new BestRouteFinderPQStrategy(start, orders, haversineCalc);

        // Alternatively:
        // BestRouteFinder bestRouteFinder = new BestRouteFinderPQStrategy(start, orders, graphCalc);

        BestRouteResult result = bestRouteFinder.findMinTime();
        if (result.getMinTime() == Double.MAX_VALUE) {
            System.out.println("No route found!");
        }
        else {
            System.out.println("Minimum total time to complete all orders: " + result.getMinTime() + " minutes");
            System.out.println("Path tp complete all orders: " + result.getPath());
        }
    }
}
