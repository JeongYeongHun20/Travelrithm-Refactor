package com.Travelrithm.service;


import com.Travelrithm.domain.PlaceEntity;
import com.Travelrithm.domain.PlanEntity;
import com.Travelrithm.domain.RegionEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.PlaceDto;
import com.Travelrithm.dto.PlanRequestDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.RegionRepository;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    public PlanResponseDto createPlan(Integer userId, PlanRequestDto planRequestDto){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당유저가 존재하지 않음"));
        RegionEntity regionEntity = regionRepository.findById(planRequestDto.regionId())
                .orElseThrow(()->new IllegalArgumentException("해당지역 존재하지 않음"));

        log.info(regionEntity.getName());

        PlanEntity planEntity=PlanEntity.builder()
                .userEntity(userEntity)
                .regionEntity(regionEntity)
                .startDate(planRequestDto.startDate())
                .endDate(planRequestDto.endDate())
                .createdAt(LocalDateTime.now())
                .transportMode(planRequestDto.transportMode())
                .startTime(planRequestDto.startTime())
                .companionType(planRequestDto.companionType())
                .companionCount(planRequestDto.companionCount())
                .travelTaste(planRequestDto.travelTaste())
                .travelPurpose(planRequestDto.travelPurpose())
                .build();

        log.info(regionEntity.getName());
        List<PlaceEntity> createPlaces = getPlaceEntities(planRequestDto, planEntity);
        planEntity.getPlaceEntities().addAll(createPlaces);


        planRepository.save(planEntity);
        return new PlanResponseDto(planEntity);
    }
    
    //필요 없을것 같지만 혹시 관리자 페이지에서 사용할 수 있을것 같아 넣어놨음
    @Transactional(readOnly = true)
    public PlanResponseDto findPlanById(Integer planId) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        return new PlanResponseDto(planEntity);
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDto> findPlans(Integer userId){
        return planRepository.findAllByUserEntity_UserId(userId).stream()
                .map(PlanResponseDto::new)
                .toList();
    }

    public PlanResponseDto updatePlan(Integer planId, PlanRequestDto planDto) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당플랜이 존재하지 않습니다"));
        planEntity.update(planDto);

        //기존 place 전부 제거
        planEntity.getPlaceEntities().clear();

        List<PlaceEntity> updatePlaces = getPlaceEntities(planDto, planEntity);

        //업데이트 된 place 저장
        planEntity.getPlaceEntities().addAll(updatePlaces);
        
        return new PlanResponseDto(planEntity);
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
                        .planEntity(planEntity)  // 양방향 관계 설정
                        .build()
                ).toList();
    }

    public void deletePlan(Integer planId){
        planRepository.deleteById(planId);
    }



}
