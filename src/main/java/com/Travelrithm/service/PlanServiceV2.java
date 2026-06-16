package com.Travelrithm.service;

import com.Travelrithm.kakaomobility.dto.WayPointResponseDto;
import com.Travelrithm.kakaomobility.dto.WaypointRequestV2;
import com.Travelrithm.planBuilderV2.CenterLocationCalculator;
import com.Travelrithm.planBuilderV2.dto.*;
import com.Travelrithm.planBuilderV2.generator.RouteSequencer;
import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.publicdata.PublicDataApiV2;
import com.Travelrithm.publicdata.dto.RegionLocationCategory;
import com.Travelrithm.publicdata.dto.RegionLocationDay;
import com.Travelrithm.publicdata.dto.RegionLocationResponse;
import com.Travelrithm.repository.PlanV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceV2 {
    private final RouteSequencer routeSequencer;
    private final PlanV2Repository planRepository;
    private final KakaoMobilityApi kakaoMobilityApi;
    private final PublicDataApiV2 publicDataApiV2;
    private final CenterLocationCalculator calculator;


    public void createPlan(List<GeneratedPlan> generatedPlans){

    }

    public List<GeneratedPlan> generatePlan(PlanGenerateRequest planGenerateRequest){
        CompletableFuture<List<SortedDayPlan>> sortedDayPlansFuture = CompletableFuture.supplyAsync(
                () -> routeSequencer.sequence(planGenerateRequest.dayMapV2s())
        );
        CompletableFuture<RegionLocationResponse> targetAreasFuture = CompletableFuture.supplyAsync(
                () -> retrieveTargetAreas(planGenerateRequest.dayMapV2s(), planGenerateRequest.preference())
        );

        List<SortedDayPlan> sortedDayPlans = sortedDayPlansFuture.join();
        RegionLocationResponse targetAreas = targetAreasFuture.join();

        return sortedDayPlans.stream()
                .map(sortedDayPlan -> new GeneratedPlan(
                        sortedDayPlan.day(),
                        sortedDayPlan.selectedPlaces(),
                        findCategoriesByDay(targetAreas, sortedDayPlan.day()),
                        retrieveRoutes(sortedDayPlan)
                ))
                .toList();
    }
    public List<GeneratedPlan> reRoutePlan(PlanGenerateRequest planGenerateRequest){
        List<SortedDayPlan> sortedDayPlans = routeSequencer.sequence(planGenerateRequest.dayMapV2s());
        return sortedDayPlans.stream()
                .map(sortedDayPlan -> new GeneratedPlan(
                        sortedDayPlan.day(),
                        sortedDayPlan.selectedPlaces(),
                        null,
                        retrieveRoutes(sortedDayPlan)
                ))
                .toList();
    }
    public RegionLocationResponse retrieveTargetAreas(List<DayMapV2> dayMapV2s, String preference){
        List<AvgCoordinate> avgCoordinates = calculateLocation(dayMapV2s);
        return publicDataApiV2.getCategory(avgCoordinates, preference);
    }

    private List<GeneratedRoute> retrieveRoutes(SortedDayPlan sortedDayPlan) {
        List<SelectedPlace> selectedPlaces = sortedDayPlan.selectedPlaces();
        if (selectedPlaces == null || selectedPlaces.size() < 2) {
            return List.of();
        }

        List<LocationV2> locations = selectedPlaces.stream()
                .map(SelectedPlace::locations)
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

    private List<RegionLocationCategory> findCategoriesByDay(RegionLocationResponse targetAreas, int day) {
        if (targetAreas == null || targetAreas.days() == null) {
            return List.of();
        }

        return targetAreas.days().stream()
                .filter(regionLocationDay -> regionLocationDay.day() == day)
                .findFirst()
                .map(RegionLocationDay::categories)
                .orElse(List.of());
    }

    private List<AvgCoordinate>  calculateLocation(List<DayMapV2> dayMapV2s){
        List<AvgCoordinate> avgCoordinates=new ArrayList<>();
        for(DayMapV2 dayMapV2: dayMapV2s){
            List<LocationV2> locations=new ArrayList<>();
            for(SelectedPlace selectedPlace: dayMapV2.selectedPlaces()){
                locations.add(selectedPlace.locations());
            }
            AvgCoordinate coordinate = calculator.calLocation(new CalculateRequestDto(dayMapV2.day(), locations));
            avgCoordinates.add(coordinate);
        }
        return avgCoordinates;

    }
    public void loadTargetAreas(){

    }

    public void generatePlan(){
    }

//    public List<PlanResponseDto> findMyPlans(Long memberId){
//        List<PlanV2> plans = planRepository.findAllByMember_MemberId(memberId);
//        return plans.stream().map(PlanResponseDto::from).toList();
//    }


}
