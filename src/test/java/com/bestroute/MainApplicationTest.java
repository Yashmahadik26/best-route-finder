package test.java.com.bestroute;

import main.java.com.bestroute.model.Location;
import main.java.com.bestroute.model.Order;
import main.java.com.bestroute.service.findbestroute.BestRouteFinder;
import main.java.com.bestroute.service.findbestroute.BestRouteFinderPQStrategy;
import main.java.com.bestroute.service.findbestroute.BestRouteResult;
import main.java.com.bestroute.service.traveltimecalculator.GraphTravelTimeCalculatorImpl;
import main.java.com.bestroute.service.traveltimecalculator.HaversineTravelTimeCalculatorImpl;
import main.java.com.bestroute.service.traveltimecalculator.TravelTimeCalculator;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MainApplicationTest {

    @Test
    public void testBestRouteFinderPQStrategyWithHaversineTimeCalculator() {
        Location start = new Location("AmanStart", 12.971598, 77.594566);
        Location restaurant1 = new Location("R1", 12.885944, 	77.597909);
        Location customer1 = new Location("C1", 12.842083, 77.600123);
        Order order1 = new Order(restaurant1, customer1, 40.0);

        Location customer2 = new Location("C2", 12.888451, 77.646292);
        Order order2 = new Order(restaurant1, customer2, 80.0);

        Order[] orders = new Order[] { order1, order2};

        // Use Haversine-based calculator.
        Set<Location> uniqueLocations = new HashSet<>();
        uniqueLocations.add(start);
        for (Order o : orders) {
            uniqueLocations.add(o.getRestaurant());
            uniqueLocations.add(o.getCustomer());
        }
        TravelTimeCalculator haversineCalc = new HaversineTravelTimeCalculatorImpl(20.0, uniqueLocations);

        // to ensure it goes from R1 -> C1 -> R1 -> C2 based on waiting time

        BestRouteFinder bestRouteFinder = new BestRouteFinderPQStrategy(start, orders, haversineCalc);

        BestRouteResult result = bestRouteFinder.findMinTime();
        // Have printed the path, not captured separately
        assertEquals(95.0, result.getMinTime(), 1.0f);
        assertEquals("R1 -> C1 -> R1 -> C2", result.getPath());
    }

    @Test
    public void testBestRouteFinderPQStrategyWithGraphTimeCalculator() {
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

        // Use a graph-based calculator.
        // For demonstration, we create an adjacency list (graph) that may be asymmetric.
        Map<Location, Map<Location, Double>> graph = new HashMap<>();
        // Example: From R1 to C1, C2, C3 are provided.
        graph.put(restaurant1, new HashMap<>());
        graph.get(restaurant1).put(customer1, 10.0);
        graph.get(restaurant1).put(customer2, 12.0);
        graph.get(restaurant1).put(customer3, 15.0);
        // From R2 to C4 is provided.
        graph.put(restaurant2, new HashMap<>());
        graph.get(restaurant2).put(customer4, 8.0);
        // Suppose from start to restaurants are provided.
        graph.put(start, new HashMap<>());
        graph.get(start).put(restaurant1, 7.0);
        graph.get(start).put(restaurant2, 9.0);

        // From customer to restaurant
        graph.put(customer1, new HashMap<>());
        graph.get(customer1).put(restaurant1, 15.0);
        graph.put(customer2, new HashMap<>());
        graph.get(customer2).put(restaurant1, 45.0);
        graph.put(customer3, new HashMap<>());
        graph.get(customer3).put(restaurant2, 55.0);
        // can have paths between customers as well

        // If a path is not in the graph, it is considered one-way (or unavailable).
        TravelTimeCalculator graphCalc = new GraphTravelTimeCalculatorImpl(graph);

        BestRouteFinder bestRouteFinder = new BestRouteFinderPQStrategy(start, orders, graphCalc);

        BestRouteResult result = bestRouteFinder.findMinTime();
        assertEquals(175.0, result.getMinTime(), 1.0f);
        assertEquals("R1 -> C1 -> R1 -> C2 -> R1 -> C3 -> R2 -> C4", result.getPath());
    }

    @Test
    public void testBestRouteFinderPQStrategyWithGraphTimeCalculatorNoPathPossible() {
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

        Order[] orders = new Order[] { order1, order2, order3 };

        // Use a graph-based calculator.
        // For demonstration, we create an adjacency list (graph) that may be asymmetric.
        Map<Location, Map<Location, Double>> graph = new HashMap<>();
        // Example: From R1 to C1, C2, C3 are provided.
        graph.put(restaurant1, new HashMap<>());
        graph.get(restaurant1).put(customer1, 10.0);
        graph.get(restaurant1).put(customer2, 12.0);
        graph.get(restaurant1).put(customer3, 15.0);
        // Suppose from start to restaurants are provided.
        graph.put(start, new HashMap<>());
        graph.get(start).put(restaurant1, 7.0);

        // If a path is not in the graph, it is considered one-way (or unavailable).
        TravelTimeCalculator graphCalc = new GraphTravelTimeCalculatorImpl(graph);

        BestRouteFinder bestRouteFinder = new BestRouteFinderPQStrategy(start, orders, graphCalc);

        BestRouteResult result = bestRouteFinder.findMinTime();
        // no path as no path from customer back to R1
        assertEquals(Double.POSITIVE_INFINITY, result.getMinTime(), 1.0f);
    }
}
