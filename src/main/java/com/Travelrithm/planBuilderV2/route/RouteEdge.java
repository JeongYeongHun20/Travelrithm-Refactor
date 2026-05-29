package com.Travelrithm.planBuilderV2.route;

public record RouteEdge(
        double distance,
        double duration
) {
    public static RouteEdge unreachable() {
        return new RouteEdge(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public double cost(RouteMetric routeMetric) {
        return switch (routeMetric) {
            case DISTANCE -> distance;
            case DURATION -> duration;
        };
    }
}
