package com.Travelrithm.planBuilderV2.generator;

import com.Travelrithm.domain.TransportMode;
import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.LocationV2;
import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;
import com.Travelrithm.planBuilderV2.route.RouteEdge;
import com.Travelrithm.planBuilderV2.route.RouteMatrixProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleGeneratorTest {

    @Test
    void sortByDistanceFromOrigin() {
        DayMapV2.Content origin = content("출발지", 126.1, 37.1);
        DayMapV2.Content far = content("먼 장소", 126.3, 37.3);
        DayMapV2.Content near = content("가까운 장소", 126.2, 37.2);
        DayMapV2 dayMap = new DayMapV2(List.of(origin, far, near), 1);
        RouteMatrixProvider routeMatrixProvider = contents -> new RouteEdge[][]{
                {new RouteEdge(0, 0), new RouteEdge(30, 300), new RouteEdge(10, 100)},
                {new RouteEdge(30, 300), new RouteEdge(0, 0), new RouteEdge(20, 200)},
                {new RouteEdge(10, 100), new RouteEdge(20, 200), new RouteEdge(0, 0)}
        };
        Generator generator = new SimpleGenerator(routeMatrixProvider);

        List<SortedDayPlan> result = generator.generatePlan(request(List.of(dayMap)));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().day()).isEqualTo(1);
        assertThat(result.getFirst().contents()).containsExactly(origin, near, far);
    }

    @Test
    void skipMatrixForSingleContent() {
        DayMapV2.Content origin = content("출발지", 126.1, 37.1);
        DayMapV2 dayMap = new DayMapV2(List.of(origin), 1);
        CountingRouteMatrixProvider routeMatrixProvider = new CountingRouteMatrixProvider();
        Generator generator = new SimpleGenerator(routeMatrixProvider);

        List<SortedDayPlan> result = generator.generatePlan(request(List.of(dayMap)));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().contents()).containsExactly(origin);
        assertThat(routeMatrixProvider.callCount).isZero();
    }

    private PlanGenerateRequest request(List<DayMapV2> dayMaps) {
        return new PlanGenerateRequest(
                dayMaps,
                "서울",
                "culture",
                "normal",
                TransportMode.car
        );
    }

    private DayMapV2.Content content(String keyword, double x, double y) {
        return new DayMapV2.Content(
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
        public RouteEdge[][] create(List<DayMapV2.Content> contents) {
            callCount++;
            return new RouteEdge[contents.size()][contents.size()];
        }
    }
}
