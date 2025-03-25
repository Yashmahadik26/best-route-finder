package test.java.com.bestroute.service.traveltimecalculator;

import main.java.com.bestroute.model.Location;
import main.java.com.bestroute.service.traveltimecalculator.GraphTravelTimeCalculatorImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GraphTravelTimeCalculatorImplTest {

    @Test
    public void testCalculateTravelTime() {
        Map<Location, Map<Location, Double>> graph = new HashMap<>();

        Location l1 = new Location("A1", 12.971598, 77.594566);
        Location l2 = new Location("R1", 12.885944, 	77.597909);
        Location l3 = new Location("R2", 12.885944, 	77.597909);

        graph.put(l1, new HashMap<>());
        graph.get(l1).put(l2, 10.0);
        graph.get(l1).put(l3, 5.0);

        graph.put(l2, new HashMap<>());
        graph.get(l2).put(l1, 2.0);
        GraphTravelTimeCalculatorImpl graphTravelTimeCalculator = new GraphTravelTimeCalculatorImpl(graph);

        assertEquals(10.0, graphTravelTimeCalculator.calculateTravelTime(l1, l2), 0.0f);
    }

    @Test
    public void testGetTravelTime() {
        Map<Location, Map<Location, Double>> graph = new HashMap<>();

        Location l1 = new Location("A1", 12.971598, 77.594566);
        Location l2 = new Location("R1", 12.885944, 	77.597909);
        Location l3 = new Location("R2", 12.885944, 	77.597909);

        graph.put(l1, new HashMap<>());
        graph.get(l1).put(l2, 10.0);
        graph.get(l1).put(l3, 5.0);

        graph.put(l2, new HashMap<>());
        graph.get(l2).put(l1, 2.0);
        GraphTravelTimeCalculatorImpl graphTravelTimeCalculator = new GraphTravelTimeCalculatorImpl(graph);

        assertEquals(10.0, graphTravelTimeCalculator.getTravelTime(l1, l2), 0.0f);
        assertEquals(2.0, graphTravelTimeCalculator.getTravelTime(l2, l1), 0.0f);
    }
}
