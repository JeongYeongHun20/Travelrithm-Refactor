package com.Travelrithm.kakaomap;



import com.Travelrithm.kakaomap.dto.KakaoPlaceRequestDto;
import com.Travelrithm.kakaomap.dto.KakaoPlaceResopnseDto;
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
