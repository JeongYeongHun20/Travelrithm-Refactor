package com.Travelrithm.planbuilder.dto.kakao.mobility;


import java.util.List;

public record WayPointResponseDto(
        String trans_id,
        List<Route> routes
) {
    public record Route(
            int result_code,
            Summary summary,
            List<Section> sections
    ) {
        public record Summary(
                Point origin,
                Point destination,
                List<Point> waypoints,
                String priority,
                Bound bound,
                int distance,
                int duration
        ) {}

        public record Bound(
                double min_x,
                double min_y,
                double max_x,
                double max_y

        ) {}
        public record Point(
                String name,
                double x,
                double y
        ) {}

        public record Section(
                int distance,
                int duration,
                List<Road> roads
        ) {
            public record Road(
                    String name,
                    int distance,
                    int duration,
                    double traffic_speed,
                    int traffic_state,
                    List<Double> vertexes
            ) {}
        }
    }
}
