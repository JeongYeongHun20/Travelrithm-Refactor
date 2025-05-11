package com.Travelrithm.service;


import com.Travelrithm.dto.RegionCountDto;
import com.Travelrithm.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final PlanRepository planRepository;


    public List<RegionCountDto> getCountRegion() {
        return planRepository.findPopularRegions();
    }

}
