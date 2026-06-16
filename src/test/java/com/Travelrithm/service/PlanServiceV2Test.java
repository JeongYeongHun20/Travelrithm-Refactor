package com.Travelrithm.service;

import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.planBuilderV2.CenterLocationCalculator;
import com.Travelrithm.planBuilderV2.dto.*;
import com.Travelrithm.planBuilderV2.generator.RouteSequencer;
import com.Travelrithm.planbuilder.dto.Location;
import com.Travelrithm.publicdata.PublicDataApiV2;
import com.Travelrithm.publicdata.dto.RegionLocation;
import com.Travelrithm.publicdata.dto.RegionLocationCategory;
import com.Travelrithm.publicdata.dto.RegionLocationDay;
import com.Travelrithm.publicdata.dto.RegionLocationResponse;
import com.Travelrithm.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceV2Test {

    @Mock
    RouteSequencer routeSequencer;
    @Mock
    KakaoMobilityApi kakaoMobilityApi;
    @Mock
    PlanRepository planRepository;
    @Mock
    PublicDataApiV2 publicDataApiV2;
    @Mock
    CenterLocationCalculator calculator;

    @InjectMocks
    PlanServiceV2 planService;

    @Test
    void createPlan() {
    }

    @Test
    void generatePlan() {
        // given
        SelectedPlace firstSelectedPlace = new SelectedPlace(
                "경복궁",
                new LocationV2(126.9769, 37.5796, "경복궁"),
                "culture",
                "고궁",
                null
        );
        SelectedPlace secondSelectedPlace = new SelectedPlace(
                "북촌한옥마을",
                new LocationV2(126.9849, 37.5826, "북촌한옥마을"),
                "culture",
                "한옥마을",
                null
        );
        DayMapV2 dayMapV2 = new DayMapV2(List.of(firstSelectedPlace, secondSelectedPlace), 1);
        List<DayMapV2> dayMapV2s=List.of(dayMapV2);
        PlanGenerateRequest request = new PlanGenerateRequest(
                List.of(dayMapV2),
                "서울",
                "culture",
                "normal",
                null
        );
        SortedDayPlan sortedDayPlan = new SortedDayPlan(
                1,
                List.of(firstSelectedPlace, secondSelectedPlace)
        );
        AvgCoordinate avgCoordinate = new AvgCoordinate(1, new Location(126.9809, 37.5811), 1000);
        RegionLocation category = new RegionLocation(
                "1",
                "국립민속박물관",
                null,
                null,
                "추천 카테고리 장소",
                new Location(126.9784, 37.5816)
        );
        RegionLocationResponse categoryResponse = new RegionLocationResponse(
                List.of(new RegionLocationDay(
                        1,
                        List.of(new RegionLocationCategory("문화 관광지", List.of(category)))
                ))
        );

        when(routeSequencer.sequence(dayMapV2s)).thenReturn(List.of(sortedDayPlan));
        when(calculator.calLocation(any())).thenReturn(avgCoordinate);
        when(publicDataApiV2.getCategory(anyList(), eq("culture"))).thenReturn(categoryResponse);

        // when
        List<GeneratedPlan> result = planService.generatePlan(request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().day()).isEqualTo(1);
        assertThat(result.getFirst().selectedPlaces()).containsExactly(firstSelectedPlace, secondSelectedPlace);
        assertThat(result.getFirst().categories()).hasSize(1);
        assertThat(result.getFirst().categories().getFirst().categoryName()).isEqualTo("문화 관광지");
        assertThat(result.getFirst().categories().getFirst().locations()).containsExactly(category);
        assertThat(result.getFirst().routes()).isEmpty();

        verify(routeSequencer).sequence(dayMapV2s);
        verify(calculator).calLocation(any());
        verify(publicDataApiV2).getCategory(anyList(), eq("culture"));
    }

    @Test
    void getPlan() {
    }
}
