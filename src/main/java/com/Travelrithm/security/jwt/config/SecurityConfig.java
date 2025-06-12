package com.Travelrithm.security.jwt.config;

import com.Travelrithm.security.jwt.JWTFilter;
import com.Travelrithm.security.jwt.JWTUtil;
import com.Travelrithm.security.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.addAllowedOrigin("http://localhost:3000");
                            config.addAllowedOrigin("https://travelrithm.kro.kr");
                            config.addAllowedMethod("*");
                            config.addAllowedHeader("*");
                            config.setAllowCredentials(true);
                            return config;
                        })
                );

        //From 로그인 방식 disable
        http.formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/join",
                                "/users",
                                "/region",
                                "/post",
                                "/api/kakao/login",
                                "/kakao/callback",
                                "/api/naver/login",
                                "/api/naver/callback",
                                "/api/social/login"
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new JWTFilter(jwtUtil),LoginFilter.class);
        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}