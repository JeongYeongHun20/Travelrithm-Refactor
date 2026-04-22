package com.Travelrithm.kakaomobility;


import com.Travelrithm.kakaomobility.dto.DestinationRequestDto;
import com.Travelrithm.kakaomobility.dto.DestinationResponseDto;
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

    private final KakaoMobilityApi kakaoMobilityApi;

    @PostMapping("/path")
    public ResponseEntity<DestinationResponseDto> findPath(@RequestBody DestinationRequestDto destinationRequestDto) {
        return ResponseEntity.ok(kakaoMobilityApi.getPath(destinationRequestDto));
    }

}
