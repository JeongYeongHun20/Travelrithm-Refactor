package com.Travelrithm.controller;

import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.AuthUser;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.dto.register.UserRegisterInfo;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.security.jwt.JWTUtil;
import com.Travelrithm.service.OAuthService;
import com.Travelrithm.service.OAuthServiceFactory;
import com.Travelrithm.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @PathVariable(name="provider") SocialType provider,
            @RequestParam(value = "state",required = false) String state){
        OAuthService service = oAuthServiceFactory.getService(provider);
        return ResponseEntity.ok(Map.of("location", service.buildAuthorizeUrl(state)));
    }

    @GetMapping("/callback/{provider}")
    public RedirectView callback(
            @PathVariable(name = "provider") SocialType provider,
            @RequestParam("code") String code,
            @RequestParam(value = "state",required = false) String state,
            HttpServletResponse response

    ){
        OAuthService service = oAuthServiceFactory.getService(provider);
        UserRegisterInfo userInfo = service.login(code, state);
        UserResponseDto user = userService.createOAuthUser(userInfo);
        String jwtToken = jwtUtil.createJwt(user.userId(),user.email(),user.nickname(),"ROLE_USER",24*60*60*1000L);

        Cookie cookie=new Cookie("accessToken",jwtToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(1800);
        response.addCookie(cookie);

        return new RedirectView(redirectUrl);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        AuthUser authUser=new AuthUser(userDetails.getUsername(),userDetails.geNickname());

        return ResponseEntity.ok(authUser);

    }



}
