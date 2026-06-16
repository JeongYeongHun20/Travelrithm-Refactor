package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.planBuilderV2.dto.*;
import com.Travelrithm.planBuilderV2.route.RouteEdge;
import com.Travelrithm.planBuilderV2.route.RouteMatrixProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleRouteSequencerTest {

    @Test
    void sortByDistanceFromOrigin() {
        SelectedPlace origin = selectedPlace("출발지", 126.1, 37.1);
        SelectedPlace far = selectedPlace("먼 장소", 126.3, 37.3);
        SelectedPlace near = selectedPlace("가까운 장소", 126.2, 37.2);
        DayMapV2 dayMapV2 = new DayMapV2(List.of(origin, far, near), 1);
        RouteMatrixProvider routeMatrixProvider = selectedPlaces -> new RouteEdge[][]{
                {new RouteEdge(0, 0), new RouteEdge(30, 300), new RouteEdge(10, 100)},
                {new RouteEdge(30, 300), new RouteEdge(0, 0), new RouteEdge(20, 200)},
                {new RouteEdge(10, 100), new RouteEdge(20, 200), new RouteEdge(0, 0)}
        };
        RouteSequencer routeSequencer = new SimpleRouteSequencer(routeMatrixProvider);

        List<SortedDayPlan> result = routeSequencer.sequence(List.of(dayMapV2));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().day()).isEqualTo(1);
        assertThat(result.getFirst().selectedPlaces()).containsExactly(origin, near, far);
    }

    @Test
    void skipMatrixForSingleContent() {
        SelectedPlace origin = selectedPlace("출발지", 126.1, 37.1);
        DayMapV2 dayMapV2 = new DayMapV2(List.of(origin), 1);
        CountingRouteMatrixProvider routeMatrixProvider = new CountingRouteMatrixProvider();
        RouteSequencer routeSequencer = new SimpleRouteSequencer(routeMatrixProvider);

        List<SortedDayPlan> result = routeSequencer.sequence(List.of(dayMapV2));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().selectedPlaces()).containsExactly(origin);
        assertThat(routeMatrixProvider.callCount).isZero();
    }


    private SelectedPlace selectedPlace(String keyword, double x, double y) {
        return new SelectedPlace(
                keyword,
                new LocationV2(x, y, keyword),
                "culture",
                keyword + " 설명",
                null
        );
    }

    private static class CountingRouteMatrixProvider implements RouteMatrixProvider {
        private int callCount;

        @Override
        public RouteEdge[][] create(List<SelectedPlace> selectedPlaces) {
            callCount++;
            return new RouteEdge[selectedPlaces.size()][selectedPlaces.size()];
        }
    }
}
