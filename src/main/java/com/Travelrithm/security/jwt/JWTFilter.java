package com.Travelrithm.security.jwt;

import com.Travelrithm.domain.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("Requested URI: {}", requestURI);


        String token =resolveTokenFromCookie(request);

        if (token == null) {
            log.info("doFilterInternal(): No JWT token found in cookies");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("authorization now");

        try {
            if (jwtUtil.isExpired(token)) {
                log.info("Token has expired");
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
        String email = jwtUtil.getUsername(token);
        Integer userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        String nickname= jwtUtil.getNickname(token);
        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .password("temppassword") //요청시마다 db룰 조회하기에 임시값을 설정한다
                .role(role)
                .build();


        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return path.startsWith("/auth/login/**") || path.startsWith("/auth/callback/**");
    }
    private String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
