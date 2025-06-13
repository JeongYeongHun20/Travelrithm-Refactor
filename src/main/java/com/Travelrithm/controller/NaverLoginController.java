package com.Travelrithm.controller;

import com.Travelrithm.dto.NaverUserResponseDto;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.security.jwt.JWTUtil;
import com.Travelrithm.service.NaverLoginService;
import com.Travelrithm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/naver")
public class NaverLoginController {
    private final NaverLoginService naverLoginService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage() {
        String naverLocation = naverLoginService.buildAuthorizeUrl();
        Map<String, String> response = new HashMap<>();
        response.put("naverLocation", naverLocation);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam("code") String code,
                                      @RequestParam("state") String state) {
        String accessToken = naverLoginService.getAccessToken(code);
        NaverUserResponseDto userInfo = naverLoginService.getUserInfo(accessToken);
        UserResponseDto userDto = userService.createUser(userInfo);
        String jwtToken = jwtUtil.createJwt(userDto.userId(),userDto.email(),"user",24*60*60*1000L);
        String redirectUrl = "https://travelrithm.kro.kr/Main?token=" + jwtToken;

        return new RedirectView(redirectUrl);
    }
}

