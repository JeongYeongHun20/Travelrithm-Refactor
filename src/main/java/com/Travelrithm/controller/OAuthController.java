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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    @Value("${travelrithm.social_redirectUrl}")
    private String redirectUrl;

    @GetMapping("/login/{provider}")
    public RedirectView login(
            @PathVariable(name="provider") SocialType provider,
            HttpServletResponse response
    ){
        OAuthService service = oAuthServiceFactory.getService(provider);
        String state = service.generateState();
        ResponseCookie cookie = ResponseCookie.from("state", state)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(600)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        String location = service.buildAuthorizeUrl(state);

        return new RedirectView(location);
    }

    @GetMapping("/callback/{provider}")
    public RedirectView callback(
            @PathVariable(name = "provider") SocialType provider,
            @RequestParam("code") String code,
            @RequestParam(value = "state",required = false) String state,
            @CookieValue(value= "state", required = false) String cookieState,
            HttpServletResponse response

    ){
        if (cookieState == null || !cookieState.equals(state)) {
            throw new RuntimeException("Invalid state");
        }
        OAuthService service = oAuthServiceFactory.getService(provider);
        UserRegisterInfo userInfo = service.login(code, state);
        UserResponseDto user = userService.createOAuthUser(userInfo);
        String jwtToken = jwtUtil.createJwt(user.userId(),user.email(),user.nickname(),"ROLE_USER",24*60*60*1000L);

        ResponseCookie cookie = ResponseCookie.from("accessToken", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(1800)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new RedirectView(redirectUrl);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        AuthUser authUser=new AuthUser(userDetails.getUserId(),userDetails.getUsername(),userDetails.geNickname());

        return ResponseEntity.ok(authUser);

    }



}
