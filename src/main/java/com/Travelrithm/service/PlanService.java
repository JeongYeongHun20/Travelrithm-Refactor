package com.Travelrithm.service;


import com.Travelrithm.domain.*;
import com.Travelrithm.domain.Plan;
import com.Travelrithm.dto.CompletPlanResponseDto;
import com.Travelrithm.dto.PlaceDto;
import com.Travelrithm.dto.PlanRequestDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.planbuilder.dto.Location;
import com.Travelrithm.kakaomobility.dto.WayPointResponseDto;
import com.Travelrithm.kakaomobility.dto.WaypointRequestDto;
import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.repository.CommunityPostRepository;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.RegionRepository;
import com.Travelrithm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final CommunityPostRepository postRepository;
    private final KakaoMobilityApi kakaoMobilityApi;

    public PlanResponseDto createPlan(Long userId, PlanRequestDto planRequestDto){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당유저가 존재하지 않음"));

        Region region = regionRepository.findById(planRequestDto.regionId())
                .orElseThrow(() -> new IllegalArgumentException("해당지역 존재하지 않음"));

        log.info(region.getAreaName());

        Plan plan = Plan.builder()
                .member(member)
                .region(region)
                .startDate(planRequestDto.startDate())
                .endDate(planRequestDto.endDate())
                .createdAt(LocalDateTime.now())
                .transportMode(planRequestDto.transportMode())
                .startTime(planRequestDto.startTime())
                .companionCount(planRequestDto.companionCount())
                .companionType(planRequestDto.companionType())
                .travelTaste(planRequestDto.travelTaste())
                .travelPurpose(planRequestDto.travelPurpose())
                .build();

        List<Place> createPlaces = getPlaceEntities(planRequestDto, plan);
        plan.getPlaceEntities().addAll(createPlaces);

        planRepository.save(plan);
        return new PlanResponseDto(plan, null); // postContent 없음
    }

    @Transactional(readOnly = true)
    public PlanResponseDto findPlanById(Integer planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        List<CommunityPost> postOpt = postRepository.findByPlan(plan);
        String postContent = postOpt.isEmpty() ? null : postOpt.get(0).getPostContent();
        return new PlanResponseDto(plan, postContent);
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDto> findPlans(Long userId) {
        List<Plan> plans = planRepository.findAllByMember_MemberId(userId);
        return plans.stream().map(plan -> {
            List<CommunityPost> postOpt = postRepository.findByPlan(plan);
            String postContent = postOpt.isEmpty() ? null : postOpt.get(0).getPostContent();
            return new PlanResponseDto(plan, postContent);
        }).toList();
    }

    public PlanResponseDto updatePlan(Integer planId, PlanRequestDto planDto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        plan.update(planDto);

        plan.getPlaceEntities().clear();
        List<Place> updatePlaces = getPlaceEntities(planDto, plan);
        plan.getPlaceEntities().addAll(updatePlaces);

        List<CommunityPost> postOpt = postRepository.findByPlan(plan);
        String postContent = postOpt.isEmpty() ? null : postOpt.get(0).getPostContent();

        return new PlanResponseDto(plan, postContent);
    }

    private static List<Place> getPlaceEntities(PlanRequestDto planDto, Plan plan) {
        return planDto.placesDto().stream()
                .map(dto -> Place.builder()
                        .placeName(dto.placeName())
                        .placeAddress(dto.placeAddress())
                        .lat(dto.lat())
                        .lng(dto.lng())
                        .memo(dto.memo())
                        .day(dto.day())
                        .sequence(dto.sequence())
                        .category(dto.category())
                        .plan(plan)
                        .build()
                ).toList();
    }

    public void deletePlan(Integer planId){
        planRepository.deleteById(planId);
    }


    public CompletPlanResponseDto planPath(Integer planId) {
        List<WayPointResponseDto> wayPointResponseDtos=new ArrayList<>();
        PlanResponseDto plan = findPlanById(planId);
        Map<Integer, List<PlaceDto>> placesByDay = plan.places().stream()
                .collect(Collectors.groupingBy(
                        PlaceDto::day,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(PlaceDto::sequence))
                                        .toList()
                        )
                ));
        for (Map.Entry<Integer, List<PlaceDto>> entry : placesByDay.entrySet()) {
            Integer day = entry.getKey();
            List<PlaceDto> dayPlaces = entry.getValue();

            if (dayPlaces.size() < 2) continue; // 경로 구성이 불가능할 경우 건너뜀

            PlaceDto origin = dayPlaces.get(0);
            PlaceDto destination = dayPlaces.get(dayPlaces.size() - 1);
            List<PlaceDto> waypoints = dayPlaces.subList(1, dayPlaces.size() - 1); // 중간 경유지

            List<Location> waypointLocations = waypoints.stream()
                    .map(p -> new Location(p.lng().doubleValue(), p.lat().doubleValue()))
                    .toList();

            // 경로 요청
            WayPointResponseDto path = kakaoMobilityApi.getPaths(new WaypointRequestDto(new Location(origin.lng().doubleValue(), origin.lat().doubleValue()),
                    new Location(destination.lng().doubleValue(), destination.lat().doubleValue()),
                    waypointLocations,2,true)
            );

            wayPointResponseDtos.add(path);
        }

        return new CompletPlanResponseDto(wayPointResponseDtos, placesByDay);

    }



}

