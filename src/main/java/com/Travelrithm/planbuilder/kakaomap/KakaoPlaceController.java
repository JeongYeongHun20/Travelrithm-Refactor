package com.Travelrithm.planbuilder.kakaomap;



import com.Travelrithm.planbuilder.dto.kakao.place.KakaoPlaceRequestDto;
import com.Travelrithm.planbuilder.dto.kakao.place.KakaoPlaceResopnseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/place-category")
@RequiredArgsConstructor
public class KakaoPlaceController {

    private final KakaoPlaceApi kakaoPlaceApi;


    @GetMapping("/place")
    public ResponseEntity<KakaoPlaceResopnseDto> getPlaceInfo(@RequestBody KakaoPlaceRequestDto kakaoPlaceRequestDto) {
        return ResponseEntity.ok(kakaoPlaceApi.getPlaceInfo(kakaoPlaceRequestDto));
    }



}
