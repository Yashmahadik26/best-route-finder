package main.java.com.bestroute.model;

/**
 * Encapsulates an order with its associated restaurant and customer locations,
 * along with the meal preparation time (in minutes) at the restaurant.
 */
public class Order {
    private final Location restaurant;
    private final Location customer;
    private final double prepTime;

    public Order(Location restaurant, Location customer, double prepTime) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.prepTime = prepTime;
    }

    public Location getRestaurant() { return restaurant; }
    public Location getCustomer() { return customer; }
    public double getPrepTime() { return prepTime; }
}