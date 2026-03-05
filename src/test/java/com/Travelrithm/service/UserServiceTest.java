package com.Travelrithm.service;

import com.Travelrithm.domain.SocialType;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.register.UserRegisterInfo;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceSocialTest {

    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks private UserService userService;

    @ParameterizedTest
    @MethodSource("provideSocialInfo")
    @DisplayName("소셜별 인터페이스 구현체에 따른 엔티티 생성 검증")
    void createOAuthUser_ShouldHandleVariousSocialProviders(
            String email, String name, SocialType socialType, String SocialId) {

        UserRegisterInfo info = mock(UserRegisterInfo.class);

        when(info.getEmail()).thenReturn(email);
        when(info.getName()).thenReturn(name);
        when(info.getSocialType()).thenReturn(socialType);
        when(info.getSocialId()).thenReturn(SocialId);
        when(info.getNickName()).thenReturn("nick_" + name);

        // 3. 기존 로직 모킹
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // 4. 실행
        UserResponseDto result = userService.createOAuthUser(info);

        // 5. 검증
        assertAll(
                () -> assertThat(result.socialType()).isEqualTo(socialType),
                () -> assertThat(result.socialId()).isEqualTo(SocialId), // "null" 문자열인지 진짜 null인지 확인
                () -> assertThat(result.email()).isEqualTo(email)
        );
    }

    private static Stream<Arguments> provideSocialInfo() {
        return Stream.of(
                // 이메일, 이름, 소셜타입, 제공ID, 예상되는SocialId문자열
                Arguments.of("google@gmail.com", "네이버", SocialType.NAVER, "123"),
                Arguments.of("kakao@kakao.com", "카카오", SocialType.KAKAO,  "456"),
                Arguments.of("local@test.com", "로컬", SocialType.LOCAL, null)
                // ↑ 현재 코드의 String.valueOf(null) 때문에 "null"이 나옴을 확인하는 케이스
        );
    }
}