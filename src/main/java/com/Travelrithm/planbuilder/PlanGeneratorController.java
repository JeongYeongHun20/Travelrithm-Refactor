package com.Travelrithm.planbuilder;


import com.Travelrithm.planbuilder.dto.front.DayMap;
import com.Travelrithm.planbuilder.dto.front.EditPlanner;
import com.Travelrithm.planbuilder.dto.front.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/geneartor")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanGeneratorController {

    private final PlanGenerator planGenerator;

    @PostMapping("/dayMap")
    public ResponseEntity<Map<String, List<DayMap.Content>>> generatePlan(@RequestBody EditPlanner editPlanner) {
        Map<String, List<DayMap.Content>> stringListMap = planGenerator.generatePlan(editPlanner);
        return ResponseEntity.ok(stringListMap);
    }




}

