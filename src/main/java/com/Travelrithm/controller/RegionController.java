package com.Travelrithm.controller;


import com.Travelrithm.dto.RegionCountDto;
import com.Travelrithm.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionCountDto>> getRegionRank() {
        return ResponseEntity.ok(regionService.getCountRegion());

    }
}
