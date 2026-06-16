package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.SelectedPlace;
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
public class SimpleRouteSequencer implements RouteSequencer {
    private final RouteMatrixProvider routeMatrixProvider;

    @Override
    public List<SortedDayPlan> sequence(List<DayMapV2> dayMapV2s) {
        return dayMapV2s.stream()
                .map(this::generateDay)
                .toList();
    }

    private SortedDayPlan generateDay(DayMapV2 dayMapV2) {
        List<SelectedPlace> selectedPlaces = dayMapV2.selectedPlaces();
        if (selectedPlaces == null || selectedPlaces.size() < 2) {
            return new SortedDayPlan(dayMapV2.day(), selectedPlaces);
        }

        RouteEdge[][] routeMatrix = routeMatrixProvider.create(selectedPlaces);
        int originIdx = 0;
        List<SelectedPlace> sortedSelectedPlaces = IntStream.range(0, selectedPlaces.size())
                .boxed()
                .sorted(Comparator.comparingDouble(i -> routeMatrix[originIdx][i].cost(RouteMetric.DISTANCE)))
                .map(selectedPlaces::get)
                .toList();

        return new SortedDayPlan(dayMapV2.day(), sortedSelectedPlaces);
    }
}
