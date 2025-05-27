package com.Travelrithm.planbuilder.dto.kakao.mobility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DestinationResponseDto(
        String trans_id,
        List<Route> routes
) {
    public record Route(
            int result_code,
            String result_msg,
            Summary summary,
            List<Section> sections,
            List<Guide> guides
    ) {
        public record Summary(
                Point origin,
                Point destination,
                List<Point> waypoints,
                String priority,
                Bound bound,
                Fare fare,
                int distance,
                int duration
        ) {
            public record Point(
                    String name,
                    double x,
                    double y
            ) {}

            public record Bound(
                    double min_x,
                    double min_y,
                    double max_x,
                    double max_y
            ) {}

            public record Fare(
                    int taxi,
                    int toll,
                    int fuel
            ) {}
        }

        public record Section(
                int distance,
                int duration,
                List<Road> roads
        ) {
            public record Road(
                    String name,
                    int type,
                    int distance,
                    int duration,
                    int traffic_speed,
                    int traffic_state,
                    Double[] vertexes
            ) {}
        }

        public record Guide(
                String name,
                double x,
                double y,
                int distance,
                int duration,
                int type,
                String guidance,
                int road_index,
                int section_index
        ) {}
    }
}
