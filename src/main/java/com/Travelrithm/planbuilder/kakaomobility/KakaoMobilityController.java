package com.Travelrithm.planbuilder.kakaomobility;


import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationRequestDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mobility")
@RequiredArgsConstructor
public class KakaoMobilityController {

    private final KakaoMobilityService kakaoMobilityService;

    @PostMapping("/path")
    public ResponseEntity<DestinationResponseDto> findPath(@RequestBody DestinationRequestDto destinationRequestDto) {
        return ResponseEntity.ok(kakaoMobilityService.getPath(destinationRequestDto));
    }

}
