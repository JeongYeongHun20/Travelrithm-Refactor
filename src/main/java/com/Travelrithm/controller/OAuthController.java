package com.Travelrithm.controller;

import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.dto.register.UserRegisterInfo;
import com.Travelrithm.security.jwt.JWTUtil;
import com.Travelrithm.service.OAuthService;
import com.Travelrithm.service.OAuthServiceFactory;
import com.Travelrithm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthServiceFactory oAuthServiceFactory;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    @Value("${travelrithm.redirectUrl}")
    private String redirectUrl;

    @PostMapping("/login/{provider}")
    public ResponseEntity<Map<String,String>> login(
            @PathVariable(name="provider") SocialType provider){
        OAuthService service = oAuthServiceFactory.getService(provider);
        return ResponseEntity.ok(Map.of("location", service.buildAuthorizeUrl()));
    }

    @GetMapping("/callback/{provider}")
    public RedirectView callback(
            @PathVariable(name = "provider") SocialType provider,
            @RequestParam("code") String code,
            @RequestParam(value = "state",required = false) String state){
        OAuthService service = oAuthServiceFactory.getService(provider);
        UserRegisterInfo userInfo = service.login(code, state);
        UserResponseDto user = userService.createOAuthUser(userInfo);
        String jwtToken = jwtUtil.createJwt(user.userId(),user.email(),"user",24*60*60*1000L); // 또는 userDto.getEmail() 등
        String finalUrl=redirectUrl+jwtToken;
        return new RedirectView(finalUrl);

    }



}
