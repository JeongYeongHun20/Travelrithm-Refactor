package com.Travelrithm.service;

import com.Travelrithm.planBuilderV2.generator.Generator;
import com.Travelrithm.kakaomobility.KakaoMobilityApi;
import com.Travelrithm.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanServiceV2Test {

    @Mock
    Generator generator;
    @Mock
    KakaoMobilityApi kakaoMobilityApi;
    @Mock
    PlanRepository planRepository;

    @InjectMocks
    PlanServiceV2 planService;

    @Test
    void savePlan() {
        planService.savePlan();
    }

    @Test
    void generatePlan() {
    }

    @Test
    void getPlan() {
    }
}