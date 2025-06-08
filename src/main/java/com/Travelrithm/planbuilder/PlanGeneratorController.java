package com.Travelrithm.planbuilder;


import com.Travelrithm.planbuilder.dto.front.CompletePlanner;
import com.Travelrithm.planbuilder.dto.front.CompleteResponseDto;
import com.Travelrithm.planbuilder.dto.front.EditPlanner;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;
import com.Travelrithm.planbuilder.dto.publicdata.TotalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
public class PlanGeneratorController {

    private final PlanGenerator planGenerator;

    @PostMapping("/dayMap")
    public ResponseEntity<TotalResponseDto> generatePlan(@RequestBody EditPlanner editPlanner) {
        TotalResponseDto stringListMap = planGenerator.generatePlan(editPlanner);
        return ResponseEntity.ok(stringListMap);
    }

    @PostMapping("/dayMap2")
    public ResponseEntity<CompleteResponseDto> completePlan(@RequestBody CompletePlanner completePlanner) {
        CompleteResponseDto completeResponseDto = planGenerator.completePlan(completePlanner);
        return ResponseEntity.ok(completeResponseDto);
    }




}

