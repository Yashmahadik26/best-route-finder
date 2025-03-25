package main.java.com.bestroute.model;

import java.util.Objects;

/**
 * Represents a geographic location with a name, latitude, and longitude.
 * Encapsulates location details used in travel time calculations.
 */
public class Location {
    private final String name;
    private final double latitude;
    private final double longitude;

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location other)) return false;
        return this.name.equals(other.name)
                && Double.compare(this.latitude, other.latitude) == 0
                && Double.compare(this.longitude, other.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude);
    }
}