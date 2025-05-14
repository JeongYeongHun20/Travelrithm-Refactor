package com.Travelrithm.service;


import com.Travelrithm.domain.RegionEntity;
import com.Travelrithm.dto.RegionDto;
import com.Travelrithm.repository.PlanRepository;
import com.Travelrithm.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final PlanRepository planRepository;
    private final RegionRepository regionRepository;

    public List<RegionDto> getCountRegion() {
        return planRepository.findPopularRegions();
    }


    public RegionDto getRegion(Integer regionId){
        RegionEntity regionEntity = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("해당지역은 존재하지 않습니다"));
        return new RegionDto(regionEntity);
    }

    public List<RegionDto> getRegions() {
        return regionRepository.findAll().stream()
                .map(RegionDto::new)
                .toList();

    }






}
