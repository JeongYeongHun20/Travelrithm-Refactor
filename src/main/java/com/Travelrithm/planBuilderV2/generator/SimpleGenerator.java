package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;
import com.Travelrithm.planBuilderV2.route.RouteEdge;
import com.Travelrithm.planBuilderV2.route.RouteMatrixProvider;
import com.Travelrithm.planBuilderV2.route.RouteMetric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SimpleGenerator implements Generator {
    private final RouteMatrixProvider routeMatrixProvider;

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

        RouteEdge[][] routeMatrix = routeMatrixProvider.create(contents);
        int originIdx = 0;
        List<DayMapV2.Content> sortedContents = IntStream.range(0, contents.size())
                .boxed()
                .sorted(Comparator.comparingDouble(i -> routeMatrix[originIdx][i].cost(RouteMetric.DISTANCE)))
                .map(contents::get)
                .toList();

        return new SortedDayPlan(dayMap.day(), sortedContents);
    }
}
