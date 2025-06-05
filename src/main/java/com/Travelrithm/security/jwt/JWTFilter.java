package com.Travelrithm.security.jwt;

import com.Travelrithm.domain.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
        String path = request.getRequestURI();

        // 인증이 필요 없는 URI는 필터 타지 않도록 예외 처리
        if (
                path.startsWith("/login") ||
                        path.startsWith("/api/login") ||
                        path.startsWith("/api/register") ||
                        path.startsWith("/api/kakao/login") ||
                        path.startsWith("/api/kakao/callback") ||
                        path.startsWith("/api/naver/login") ||
                        path.startsWith("/api/naver/callback") ||
                        path.startsWith("/api/social/login")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }
        log.info("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            log.info("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }
        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        Integer userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .name(username)
                .password("temppassword") //요청시마다 db룰 조회하기에 임시값을 설정한다
                .role(role)
                .build();
        

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
