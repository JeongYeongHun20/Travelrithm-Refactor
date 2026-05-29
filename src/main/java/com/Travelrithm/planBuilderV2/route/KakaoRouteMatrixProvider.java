package com.Travelrithm.planBuilderV2.route;

import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.kakaomobility.dto.WayPointResponseDto;
import com.Travelrithm.kakaomobility.dto.WaypointRequestV2;
import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.LocationV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoRouteMatrixProvider implements RouteMatrixProvider {
    private final KakaoMobilityApi kakaoMobilityApi;

    @Override
    public RouteEdge[][] create(List<DayMapV2.Content> contents) {
        int size = contents.size();
        RouteEdge[][] routeMatrix = new RouteEdge[size][size];

        for (int originIdx = 0; originIdx < size; originIdx++) {
            for (int destinationIdx = 0; destinationIdx < size; destinationIdx++) {
                if (originIdx == destinationIdx) {
                    routeMatrix[originIdx][destinationIdx] = new RouteEdge(0, 0);
                    continue;
                }

                LocationV2 origin = contents.get(originIdx).locations();
                LocationV2 destination = contents.get(destinationIdx).locations();
                routeMatrix[originIdx][destinationIdx] = getRouteEdge(origin, destination);
            }
        }

        return routeMatrix;
    }

    private RouteEdge getRouteEdge(LocationV2 origin, LocationV2 destination) {
        WayPointResponseDto response = kakaoMobilityApi.getPathsV2(
                new WaypointRequestV2(origin, destination, List.of(), 2, true)
        );

        if (response == null || response.routes() == null || response.routes().isEmpty()) {
            return RouteEdge.unreachable();
        }

        WayPointResponseDto.Route.Summary summary = response.routes().getFirst().summary();
        return new RouteEdge(summary.distance(), summary.duration());
    }
}
