package main.java.com.bestroute.service.findbestroute;

public class BestRouteResult {

    private final double minTime;
    private final String path;

    public BestRouteResult(double minTime, String path) {
        this.minTime = minTime;
        this.path = path;
    }

    public double getMinTime() {
        return minTime;
    }

    public String getPath() {
        return path;
    }
}
