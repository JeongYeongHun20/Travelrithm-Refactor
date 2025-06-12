package com.Travelrithm.controller;


import com.Travelrithm.dto.KakaoUserResponseDto;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.security.jwt.JWTUtil;
import com.Travelrithm.service.KakaoLoginService;
import com.Travelrithm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kakao")
@Slf4j
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage() {
        String location = kakaoLoginService.buildAuthorizeUrl();
        Map<String, String> response = new HashMap<>();
        response.put("location",location);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam("code") String code){
        String accessToken = kakaoLoginService.getAccessToken(code);
        KakaoUserResponseDto userInfo = kakaoLoginService.getUserInfo(accessToken);
        UserResponseDto userDto = userService.createUser(userInfo);
        String jwtToken = jwtUtil.createJwt(userDto.userId(),userDto.email(),"user",24*60*60*1000L); // 또는 userDto.getEmail() 등

        String redirectUrl = "https://travelrithm.kro.kr/Main?token=" + jwtToken;

        return new RedirectView(redirectUrl);
//        return ResponseEntity.ok(userDto);
    }



}
