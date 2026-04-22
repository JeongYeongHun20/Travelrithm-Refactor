package com.Travelrithm.service;

import com.Travelrithm.planbuilder.dto.EditPlanner;
import com.Travelrithm.planBuilderV2.generator.Generator;
import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanServiceV2 {
    private final Generator generator;
    private final PlanRepository planRepository;
    private final KakaoMobilityApi kakaoMobilityApi;

    public void savePlan(){

    }
    public void generatePlan(){
        generator.generatePlan();
    }
    public void getPlan(EditPlanner editPlanner){

    }


}
