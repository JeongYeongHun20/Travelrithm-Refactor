package com.Travelrithm.service;


import com.Travelrithm.domain.SocialType;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.KakaoUserResponseDto;
import com.Travelrithm.dto.NaverUserResponseDto;
import com.Travelrithm.dto.UserRequestDto;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponseDto createUser(KakaoUserResponseDto kakaoUserInfo) {
        UserEntity user = userRepository.findByEmail(kakaoUserInfo.kakao_account().email())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .socialId(String.valueOf(kakaoUserInfo.id()))
                            .socialType(SocialType.kakao)
                            .name(kakaoUserInfo.kakao_account().profile().nickname())
                            .email(kakaoUserInfo.kakao_account().email())
                            .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                            .nickname(kakaoUserInfo.kakao_account().profile().nickname())
                            .build();
                    return userRepository.save(newUser);
                });
        return new UserResponseDto(user);
    }

    public UserResponseDto createUser(NaverUserResponseDto naverUserInfo) {
        UserEntity user = userRepository.findByEmail(naverUserInfo.response().email())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .socialId(naverUserInfo.response().id())
                            .socialType(SocialType.naver)
                            .name(naverUserInfo.response().name())
                            .email(naverUserInfo.response().email())
                            .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                            .nickname(naverUserInfo.response().nickname())
                            .build();
                    return userRepository.save(newUser);
                });

        return new UserResponseDto(user);
    }

    public UserResponseDto createUser(UserRequestDto localUserInfo) {
        UserEntity userEntity = UserEntity.builder()
                .name(localUserInfo.name())
                .password(bCryptPasswordEncoder.encode(localUserInfo.password()))
                .email(localUserInfo.email())
                .nickname(localUserInfo.nickname())
                .socialType(SocialType.local)
                .build();
        validateDuplicateEmail(userEntity);
        userRepository.save(userEntity);

        return new UserResponseDto(userEntity);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUser(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public UserResponseDto updateUser(Integer userId, UserRequestDto updatedUserDto){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다"));
        userEntity.update(updatedUserDto);
        return new UserResponseDto(userEntity);
    }

    private void validateDuplicateEmail(UserEntity user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(i-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }



}
