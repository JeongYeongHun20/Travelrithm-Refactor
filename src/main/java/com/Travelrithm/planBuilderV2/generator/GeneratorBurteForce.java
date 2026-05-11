package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.kakaomobility.dto.WayPointResponseDto;
import com.Travelrithm.kakaomobility.dto.WaypointRequestV2;
import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.LocationV2;
import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GeneratorBurteForce implements Generator {
    private final KakaoMobilityApi kakaoMobilityApi;

    @Override
    public List<SortedDayPlan> generatePlan(PlanGenerateRequest planGenerateRequest) {
        return planGenerateRequest.dayMapList().stream()
                .map(this::generateDay)
                .toList();
    }

    private SortedDayPlan generateDay(DayMapV2 dayMap) {
        List<DayMapV2.Content> contents = dayMap.content();
        if (contents == null || contents.size() < 2) {
            return new SortedDayPlan(dayMap.day(), contents);
        }

        double[][] distanceMatrix = createDistanceMatrix(contents);
        int originIdx = 0;
        List<DayMapV2.Content> sortedContents = IntStream.range(0, contents.size())
                .boxed()
                .sorted(Comparator.comparingDouble(i -> distanceMatrix[originIdx][i]))
                .map(contents::get)
                .toList();

        return new SortedDayPlan(dayMap.day(), sortedContents);
    }

    private double[][]
    createDistanceMatrix(List<DayMapV2.Content> contents) {
        int size = contents.size();
        double[][] distanceMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }

                LocationV2 origin = contents.get(i).locations();
                LocationV2 destination = contents.get(j).locations();
                distanceMatrix[i][j] = getDistance(origin, destination);
            }
        }

        return distanceMatrix;
    }

    private double getDistance(LocationV2 origin, LocationV2 destination) {
        WayPointResponseDto response = kakaoMobilityApi.getPathsV2(
                new WaypointRequestV2(origin, destination, List.of(), 2, true)
        );

        if (response == null || response.routes() == null || response.routes().isEmpty()) {
            return Double.MAX_VALUE;
        }

        return response.routes().getFirst().summary().distance();
    }
}
