package com.Travelrithm.service;


import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.PlaceEntity;
import com.Travelrithm.domain.PlanEntity;
import com.Travelrithm.domain.RegionEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.PlaceDto;
import com.Travelrithm.dto.PlanRequestDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.repository.CommunityPostRepository;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.RegionRepository;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final CommunityPostRepository postRepository;

    public PlanResponseDto createPlan(Integer userId, PlanRequestDto planRequestDto){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당유저가 존재하지 않음"));

        RegionEntity regionEntity = regionRepository.findById(planRequestDto.regionId())
                .orElseThrow(() -> new IllegalArgumentException("해당지역 존재하지 않음"));

        log.info(regionEntity.getName());

        PlanEntity planEntity = PlanEntity.builder()
                .userEntity(userEntity)
                .regionEntity(regionEntity)
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

        List<PlaceEntity> createPlaces = getPlaceEntities(planRequestDto, planEntity);
        planEntity.getPlaceEntities().addAll(createPlaces);

        planRepository.save(planEntity);
        return new PlanResponseDto(planEntity, null); // postContent 없음
    }

    @Transactional(readOnly = true)
    public PlanResponseDto findPlanById(Integer planId) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        Optional<CommunityPostEntity> postOpt = postRepository.findByPlanEntity(planEntity);
        String postContent = postOpt.map(CommunityPostEntity::getPostContent).orElse(null);
        return new PlanResponseDto(planEntity, postContent);
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDto> findPlans(Integer userId) {
        List<PlanEntity> plans = planRepository.findAllByUserEntity_UserId(userId);
        return plans.stream().map(plan -> {
            Optional<CommunityPostEntity> postOpt = postRepository.findByPlanEntity(plan);
            String postContent = postOpt.map(CommunityPostEntity::getPostContent).orElse(null);
            return new PlanResponseDto(plan, postContent);
        }).toList();
    }

    public PlanResponseDto updatePlan(Integer planId, PlanRequestDto planDto) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        planEntity.update(planDto);

        planEntity.getPlaceEntities().clear();
        List<PlaceEntity> updatePlaces = getPlaceEntities(planDto, planEntity);
        planEntity.getPlaceEntities().addAll(updatePlaces);

        Optional<CommunityPostEntity> postOpt = postRepository.findByPlanEntity(planEntity);
        String postContent = postOpt.map(CommunityPostEntity::getPostContent).orElse(null);

        return new PlanResponseDto(planEntity, postContent);
    }

    private static List<PlaceEntity> getPlaceEntities(PlanRequestDto planDto, PlanEntity planEntity) {
        return planDto.placesDto().stream()
                .map(dto -> PlaceEntity.builder()
                        .placeName(dto.placeName())
                        .placeAddress(dto.placeAddress())
                        .lat(dto.lat())
                        .lng(dto.lng())
                        .memo(dto.memo())
                        .day(dto.day())
                        .sequence(dto.sequence())
                        .category(dto.category())
                        .planEntity(planEntity)
                        .build()
                ).toList();
    }

    public void deletePlan(Integer planId){
        planRepository.deleteById(planId);
    }
}

