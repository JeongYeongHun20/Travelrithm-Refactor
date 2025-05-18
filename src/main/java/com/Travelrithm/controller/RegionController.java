package com.Travelrithm.controller;


import com.Travelrithm.dto.RegionDto;
import com.Travelrithm.dto.RegionResponseDto;
import com.Travelrithm.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/rank")
    public ResponseEntity<List<RegionDto>> getRegionRank() {
        return ResponseEntity.ok(regionService.getCountRegion());

    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionDto> getRegion(@PathVariable(name = "regionId") Integer regionId) {
        return ResponseEntity.ok(regionService.getRegion(regionId));
    }

    @GetMapping("/name/{regionName}")
    public ResponseEntity<List<RegionDto>> getRegionByName(@PathVariable String regionName) {
        List<RegionDto> regionsDtos = regionService.getRegionByName(regionName);
        return ResponseEntity.ok(regionsDtos);
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionDto>> getRegions() {
        return ResponseEntity.ok(regionService.getRegions());
    }

    @GetMapping("/simple") // name/context/image/code 넘김
    public ResponseEntity<List<RegionResponseDto>> getSimpleRegions() {
        return ResponseEntity.ok(regionService.getSimpleRegions());
    }



}
