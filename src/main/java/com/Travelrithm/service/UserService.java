package com.Travelrithm.service;


import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.*;
import com.Travelrithm.dto.register.UserRegisterInfo;
import com.Travelrithm.dto.register.UserRequestDto;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserResponseDto createOAuthUser(UserRegisterInfo userRegisterInfo){
        UserEntity user = userRepository.findByEmail(userRegisterInfo.getEmail())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .socialId(userRegisterInfo.getSocialId())
                            .socialType(userRegisterInfo.getSocialType())
                            .name(userRegisterInfo.getName())
                            .email(userRegisterInfo.getEmail())
                            .nickname(userRegisterInfo.getNickName())
                            .build();
                    return userRepository.save(newUser);
                });

        return new UserResponseDto(user);/*** 차후에 userNotfoundException에러를 던져야됨***/
    }
    public UserResponseDto createUser(UserRequestDto userRegisterInfo){
        UserEntity user = userRepository.findByEmail(userRegisterInfo.email())
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .socialId(null)
                            .socialType(userRegisterInfo.socialType())
                            .name(userRegisterInfo.name())
                            .email(userRegisterInfo.email())
                            .password(bCryptPasswordEncoder.encode(userRegisterInfo.password()))
                            .nickname(userRegisterInfo.nickname())
                            .build();
                    return userRepository.save(newUser);
                });

        return new UserResponseDto(user);/*** 차후에 userNotfoundException에러를 던져야됨***/
    }


    @Transactional(readOnly = true)
    public UserResponseDto findUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserResponseDto(user);
    }
    public UserResponseDto findUser(String email){
        UserEntity user=userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자 입니다. "));
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto updatedUserDto){
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
