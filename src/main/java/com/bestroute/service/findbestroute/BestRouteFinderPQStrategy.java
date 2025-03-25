package main.java.com.bestroute.service.findbestroute;

import main.java.com.bestroute.model.Location;
import main.java.com.bestroute.model.Order;
import main.java.com.bestroute.service.traveltimecalculator.TravelTimeCalculator;

import java.util.*;

/**
 * Uses a best-first search (via a priority queue) to solve the pickup-and-delivery problem.
 * The travel time between locations is computed using a provided TravelTimeCalculator strategy.
 * <p>
 * Each search state is defined by:
 *   - Current location (Location)
 *   - Bitmask for orders picked up
 *   - Bitmask for orders delivered
 *   - Accumulated time (in minutes)
 *   - route taken to reach that state.
 * <p>
 * Moves:
 *   • Pickup move: For each order not yet picked up, move to its restaurant, waiting if needed.
 *   • Delivery move: For each order picked up but not delivered, move to its customer.
 * <p>
 * State dominance is maintained by storing the best time for each state (current location, picked, delivered).
 */
public class BestRouteFinderPQStrategy implements BestRouteFinder {

    private final Location start;
    private final Order[] orders;
    private final int n; // number of orders
    private final TravelTimeCalculator travelTimeCalculator;

    // Maps state key (current location, picked, delivered) to best time reached.
    private final Map<StateKey, Double> bestTimeMap = new HashMap<>();

    public BestRouteFinderPQStrategy(Location start, Order[] orders, TravelTimeCalculator travelTimeCalculator) {
        this.start = start;
        this.orders = orders;
        this.travelTimeCalculator = travelTimeCalculator;
        n = orders.length;;
    }

    /**
     * Finds the minimum total time to complete all orders.
     */
    public BestRouteResult findMinTime() {
        PriorityQueue<PQState> pq = new PriorityQueue<>();
        PQState startState = new PQState(start, 0, 0, 0.0);
        pq.add(startState);
        bestTimeMap.put(new StateKey(start, 0, 0), 0.0);

        while (!pq.isEmpty()) {
            PQState state = pq.poll();
            // If all orders delivered, return the accumulated time.
            if (state.delivered == (1 << n) - 1) {
                String path = state.route.isEmpty() ? "" : String.join(" -> ", state.route);
                return new BestRouteResult(state.time, path);
            }

            // Expand pickup moves.
            // Can move to any restaurant to pick the meal (wait in case needed)
            for (int i = 0; i < n; i++) {
                if ((state.picked & (1 << i)) == 0) {  // Order 'i' not picked up
                    Location restaurant = orders[i].getRestaurant();
                    double travel = travelTimeCalculator.getTravelTime(state.current, restaurant);
                    double arrivalTime = state.time + travel;
                    double waitTime = Math.max(0, orders[i].getPrepTime() - arrivalTime);
                    double serviceTime = arrivalTime + waitTime;
                    int newPicked = state.picked | (1 << i);
                    List<String> newRoute = new ArrayList<>(state.route);
                    newRoute.add(restaurant.getName());
                    // for debugging, can use below string
                    // newRoute.add(String.format("At %.2f min: Pick up Order %d at %s (arrived %.2f, waited %.2f)",
                    //        serviceTime, i+1, restaurant.getName(), arrivalTime, waitTime));
                    PQState newState = new PQState(restaurant, newPicked, state.delivered, serviceTime, newRoute);
                    tryAddState(newState, pq);
                }
            }

            // Expand delivery moves.
            for (int i = 0; i < n; i++) {
                if ((state.picked & (1 << i)) != 0 && (state.delivered & (1 << i)) == 0) {  // Order i picked up but not delivered
                    Location customer = orders[i].getCustomer();
                    double travel = travelTimeCalculator.getTravelTime(state.current, customer);
                    double newTime = state.time + travel;
                    int newDelivered = state.delivered | (1 << i);
                    List<String> newRoute = new ArrayList<>(state.route);
                    newRoute.add(customer.getName());
                    // for debugging, can use below string
                    // newRoute.add(String.format("At %.2f min: Deliver Order %d at %s",
                    //        newTime, i+1, customer.getName()));
                    PQState newState = new PQState(customer, state.picked, newDelivered, newTime, newRoute);
                    tryAddState(newState, pq);
                }
            }
        }
        return new BestRouteResult(Double.MAX_VALUE, ""); // No solution found.
    }

    private void tryAddState(PQState newState, PriorityQueue<PQState> pq) {
        StateKey key = new StateKey(newState.current, newState.picked, newState.delivered);
        if (!bestTimeMap.containsKey(key) || newState.time < bestTimeMap.get(key)) {
            bestTimeMap.put(key, newState.time);
            pq.add(newState);
        }
    }
}
