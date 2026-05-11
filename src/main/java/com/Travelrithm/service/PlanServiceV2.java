package com.Travelrithm.service;

import com.Travelrithm.domain.Plan;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.kakaomobility.dto.WayPointResponseDto;
import com.Travelrithm.kakaomobility.dto.WaypointRequestV2;
import com.Travelrithm.planBuilderV2.CenterLocationCalculator;
import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.planBuilderV2.dto.CalculateRequestDto;
import com.Travelrithm.planBuilderV2.dto.DayMapV2;
import com.Travelrithm.planBuilderV2.dto.GeneratedPlan;
import com.Travelrithm.planBuilderV2.dto.GeneratedRoute;
import com.Travelrithm.planBuilderV2.dto.LocationV2;
import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;
import com.Travelrithm.planBuilderV2.generator.Generator;
import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.publicdata.PublicDataApiV2;
import com.Travelrithm.publicdata.dto.RegionLocation;
import com.Travelrithm.publicdata.dto.RegionLocationDay;
import com.Travelrithm.publicdata.dto.RegionLocationResponse;
import com.Travelrithm.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceV2 {
    private final Generator generator;
    private final PlanRepository planRepository;
    private final KakaoMobilityApi kakaoMobilityApi;
    private final PublicDataApiV2 publicDataApiV2;
    private final CenterLocationCalculator calculator;


    public void createPlan(List<GeneratedPlan> generatedPlans){
    }

    public List<GeneratedPlan> generatePlan(PlanGenerateRequest planGenerateRequest){
        CompletableFuture<List<SortedDayPlan>> sortedDayPlansFuture = CompletableFuture.supplyAsync(
                () -> generator.generatePlan(planGenerateRequest)
        );
        CompletableFuture<RegionLocationResponse> targetAreasFuture = CompletableFuture.supplyAsync(
                () -> retrieveTargetAreas(planGenerateRequest.dayMapList(), planGenerateRequest.preference())
        );

        List<SortedDayPlan> sortedDayPlans = sortedDayPlansFuture.join();
        RegionLocationResponse targetAreas = targetAreasFuture.join();

        return sortedDayPlans.stream()
                .map(sortedDayPlan -> new GeneratedPlan(
                        sortedDayPlan.day(),
                        sortedDayPlan.contents(),
                        findCategoriesByDay(targetAreas, sortedDayPlan.day()),
                        retrieveRoutes(sortedDayPlan)
                ))
                .toList();
    }
    public List<SortedDayPlan> reRoutePlan(PlanGenerateRequest planGenerateRequest){
        return generator.generatePlan(planGenerateRequest);
    }
    public RegionLocationResponse retrieveTargetAreas(List<DayMapV2> dayMaps, String preference){
        List<AvgCoordinate> avgCoordinates = calculateLocation(dayMaps);
        return publicDataApiV2.getCategory(avgCoordinates, preference);
    }

    private List<GeneratedRoute> retrieveRoutes(SortedDayPlan sortedDayPlan) {
        List<DayMapV2.Content> contents = sortedDayPlan.contents();
        if (contents == null || contents.size() < 2) {
            return List.of();
        }

        List<LocationV2> locations = contents.stream()
                .map(DayMapV2.Content::locations)
                .toList();
        WayPointResponseDto response = kakaoMobilityApi.getPathsV2(
                new WaypointRequestV2(
                        locations.getFirst(),
                        locations.getLast(),
                        locations.subList(1, locations.size() - 1),
                        2,
                        true
                )
        );

        if (response == null) {
            return List.of();
        }
        return List.of(new GeneratedRoute(response));
    }

    private List<RegionLocation> findCategoriesByDay(RegionLocationResponse targetAreas, int day) {
        if (targetAreas == null || targetAreas.days() == null) {
            return List.of();
        }

        return targetAreas.days().stream()
                .filter(regionLocationDay -> regionLocationDay.day() == day)
                .findFirst()
                .map(RegionLocationDay::locations)
                .orElse(List.of());
    }

    private List<AvgCoordinate>  calculateLocation(List<DayMapV2> dayMaps){
        List<AvgCoordinate> avgCoordinates=new ArrayList<>();
        for(DayMapV2 dayMap: dayMaps){
            List<LocationV2> locations=new ArrayList<>();
            for(DayMapV2.Content content: dayMap.content()){
                locations.add(content.locations());
            }
            AvgCoordinate coordinate = calculator.calLocation(new CalculateRequestDto(dayMap.day(), locations));
            avgCoordinates.add(coordinate);
        }
        return avgCoordinates;

    }
    public void loadTargetAreas(){

    }

    public void generatePlan(){
    }

    public List<PlanResponseDto> findMyPlans(Long memberId){
        List<Plan> plans = planRepository.findAllByMember_MemberId(memberId);
        return plans.stream().map(PlanResponseDto::from).toList();
    }


}
