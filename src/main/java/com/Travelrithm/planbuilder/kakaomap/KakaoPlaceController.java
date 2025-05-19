package com.Travelrithm.planbuilder.kakaomap;



import com.Travelrithm.planbuilder.dto.KakaoPlaceRequestDto;
import com.Travelrithm.planbuilder.dto.KakaoPlaceResopnseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/place-category")
@RequiredArgsConstructor
public class KakaoPlaceController {

    private final KakaoPlaceService kakaoPlaceService;


    @GetMapping("/place")
    public ResponseEntity<KakaoPlaceResopnseDto> getPlaceInfo(@RequestBody KakaoPlaceRequestDto kakaoPlaceRequestDto) {
        return ResponseEntity.ok(kakaoPlaceService.getPlaceInfo(kakaoPlaceRequestDto));
    }

}
