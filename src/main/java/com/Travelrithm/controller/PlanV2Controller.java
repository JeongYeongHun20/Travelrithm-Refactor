package com.Travelrithm.controller;

import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.planBuilderV2.dto.GeneratedPlan;
import com.Travelrithm.planBuilderV2.dto.PlanGenerateRequest;
import com.Travelrithm.planBuilderV2.dto.SortedDayPlan;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.service.PlanServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/plans")
@RequiredArgsConstructor
public class PlanV2Controller {
    private final PlanServiceV2 planServiceV2;

//    @GetMapping
//    public ResponseEntity<List<PlanResponseDto>> getMyPlans(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        return ResponseEntity.ok(planServiceV2.findMyPlans(userDetails.getMemberId()));
//    }

    @PostMapping("/drafts")
    public ResponseEntity<List<GeneratedPlan>> createPlanDraft(@RequestBody PlanGenerateRequest planGenerateRequest) {
        return ResponseEntity.ok(planServiceV2.generatePlan(planGenerateRequest));
    }

    @PostMapping("/plan")
        public void savePlan(@RequestBody List<GeneratedPlan> generatedPlans){
            planServiceV2.createPlan(generatedPlans);
        }


    @PostMapping("/reroute")
    public ResponseEntity<List<GeneratedPlan>> reRoutePlan(@RequestBody PlanGenerateRequest planGenerateRequest){
        List<GeneratedPlan> generatedPlans = planServiceV2.reRoutePlan(planGenerateRequest);
        return ResponseEntity.ok(generatedPlans);
    }

}
