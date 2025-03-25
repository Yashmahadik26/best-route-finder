package test.java.com.bestroute.service.traveltimecalculator;

import main.java.com.bestroute.model.Location;
import main.java.com.bestroute.service.traveltimecalculator.HaversineTravelTimeCalculatorImpl;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class HaversineTravelTimeCalculatorImplTest {

    @Test
    public void testCalculateTravelTime() {

        Location l1 = new Location("A1", 12.971598, 77.594566);
        Location l2 = new Location("R1", 12.885944, 	77.597909);

        HashSet<Location> locations = new HashSet<>();
        locations.add(l1);
        locations.add(l2);

        HaversineTravelTimeCalculatorImpl haversineTravelTimeCalculator = new HaversineTravelTimeCalculatorImpl(1, locations);
        // ~10 km distance between the 2
        assertEquals(571, haversineTravelTimeCalculator.calculateTravelTime(l1, l2), 1.0f);
    }

    @Test
    public void testGetTravelTime() {
        Location l1 = new Location("A1", 12.971598, 77.594566);
        Location l2 = new Location("R1", 12.885944, 	77.597909);

        HashSet<Location> locations = new HashSet<>();
        locations.add(l1);
        locations.add(l2);

        HaversineTravelTimeCalculatorImpl haversineTravelTimeCalculator = new HaversineTravelTimeCalculatorImpl(1, locations);

        // ~10 km distance between the 2
        assertEquals(571, haversineTravelTimeCalculator.getTravelTime(l1, l2), 1.0f);
    }
}
