Overview:
---------
This project demonstrates an extensible best-first search solution for a pickup-and-delivery problem using the haversine formula for travel time calculations.
The solution computes not only the minimum total time but also reconstructs the sequence of locations traversed (the route).

Project Structure:
------------------
main.java.com.bestroute.model
- Location.java
- Order.java

main.java.com.bestroute.service.traveltimecalculator
- TravelTimeCalculator.java                (interface)
- HaversineTravelTimeCalculatorImpl.java   (approach provided in the question)
- GraphTravelTimeCalculatorImpl.java       (another approach in case adjacency list/graph is given with time to travel between some locations)

main.java.com.service.findbestroute
- BestRouteFinder  (interface)
- BestRouteFinderPQStrategy.java  (returns the minimum time for delivery executive to pickup and deliver all meals)
- PQState.java    (represents search states and carries the route taken)
- StateKey.java   (used for state memoization)
- BestRouteResult (used to capture the final result - minimum time to deliver all orders + the route used)

Approach:
-------------
- Extensible way to use different approaches to calculate time taken to travel between locations.
- Based on haversine approach 
    - won't be visiting a location without any service, i.e. restaurant would be visited for pickup & customer to deliver meal
- in real life 
  - it can happen that a certain path is a one way path or it takes longer
  - in that case we can revisit a certain location again just to find the route with shortest possible time. 
    - so 3 options from a certain state to take min time approach
      - go to a customer/restaurant without delivering or pickup service
      - go to a restaurant to pick up a meal
      - go to a customer to deliver meal
- bit masking is used as a delivery executive wont have a lot of orders to work with in parallel, so can be stored in integer
- memoization used so that future states that reach the same configuration with worse time are pruned
- Reason for having current location + picked bit mask + delivered bit mask --
  - {1}->{1,0}->{1,0,1} = (This is possible as by visiting 1 again (currently current node is 1) this could lead to some other path which would have connected to 1 only)
  - {1}->{1,0}->{1,0,1} -> {1,0,1,0} = (This shouldn't be visited) as by including 0 again, this is just repetition of 0 and 1 again in other words, by including 0 again resulting in a state we already visited

Key Features:
-------------
- Best-first search (using a priority queue) to explore the state space in order of increasing time.
- PQState includes a route (a list of action descriptions) to reconstruct the best route.
- Uses the haversine formula (via HaversineTravelTimeCalculator) to calculate travel times.
- Modular and extensible design that allows swapping out travel time strategies.

Usage:
------
Run the main method in MainApplication to see the output, which displays the minimum total time
and the sequence of actions (route) taken.

Assumptions:
------
- Since geolocations are provided, assumed delivery executive can move from any location to any location (hence, bi-directional as well).

