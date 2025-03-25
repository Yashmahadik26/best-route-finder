package main.java.com.bestroute.service.findbestroute;

import main.java.com.bestroute.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a search state for the best-first search using priority queue.
 * In addition to current location, picked and delivered bitmasks, and accumulated time,
 * it stores a route (a list of actions) that led to this state.
 */
public class PQState implements Comparable<PQState> {
    public final Location current;
    public final int picked;     // Bitmask of orders picked up.
    public final int delivered;  // Bitmask of orders delivered.
    public final double time;    // Accumulated time in minutes.
    public final List<String> route; // Describes the sequence of actions taken.

    public PQState(Location current, int picked, int delivered, double time, List<String> route) {
        this.current = current;
        this.picked = picked;
        this.delivered = delivered;
        this.time = time;
        this.route = route;
    }

    // Convenience constructor when no route exists yet.
    public PQState(Location current, int picked, int delivered, double time) {
        this(current, picked, delivered, time, new ArrayList<>());
    }

    @Override
    public int compareTo(PQState other) {
        return Double.compare(this.time, other.time);
    }
}