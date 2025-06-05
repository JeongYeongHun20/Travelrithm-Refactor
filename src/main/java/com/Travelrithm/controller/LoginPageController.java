package com.Travelrithm.controller;

import com.Travelrithm.service.KakaoLoginService;
import com.Travelrithm.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/social")
public class LoginPageController {

    private final NaverLoginService naverLoginService;
    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage() {
        Map<String, String> response = new HashMap<>();
        response.put("naverLocation", naverLoginService.buildAuthorizeUrl());
        response.put("location", kakaoLoginService.buildAuthorizeUrl());
        return ResponseEntity.ok(response);
    }
}
