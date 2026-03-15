package com.Travelrithm.controller;



import com.Travelrithm.dto.CompletPlanResponseDto;
import com.Travelrithm.dto.PlanRequestDto;
import com.Travelrithm.dto.PlanResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WaypointRequestDto;
import com.Travelrithm.planbuilder.kakaomobility.KakaoMobilityService;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @GetMapping("myPlans")
    public ResponseEntity<List<PlanResponseDto>> myPlans(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(planService.findPlans(userId));
    }

    @PostMapping("/createPlan")
    public ResponseEntity<PlanResponseDto> createPlan(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PlanRequestDto planDto) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(planService.createPlan(userId, planDto));


    }

    //검색 기능이니까 PlanName등으로 바꿔야 함
    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponseDto> findPlan(@PathVariable(name = "planId") Integer planId) {
        return ResponseEntity.ok(planService.findPlanById(planId));
    }

    @PutMapping("{planId}")
    public ResponseEntity<PlanResponseDto> updatePlan(@PathVariable(name = "planId") Integer planId, @RequestBody PlanRequestDto planDto) {
        return ResponseEntity.ok(planService.updatePlan(planId, planDto));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable(name = "planId") Integer planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planId}/path")
    public ResponseEntity<CompletPlanResponseDto> getPathPlan(@PathVariable(name = "planId") Integer planId) {
        return ResponseEntity.ok(planService.planPath(planId));

    }

}
