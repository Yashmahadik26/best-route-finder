package main.java.com.bestroute.service.findbestroute;

import main.java.com.bestroute.model.Location;

import java.util.Objects;

/**
 * Encapsulates a state (current location, picked bitmask, delivered bitmask)
 * for use in state dominance (memoization).
 */
public class StateKey {
    private final Location current;
    private final int picked;
    private final int delivered;

    public StateKey(Location current, int picked, int delivered) {
        this.current = current;
        this.picked = picked;
        this.delivered = delivered;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StateKey other)) return false;
        return this.picked == other.picked &&
                this.delivered == other.delivered &&
                this.current.equals(other.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, picked, delivered);
    }
}