package com.Travelrithm.service;


import com.Travelrithm.domain.Member;
import com.Travelrithm.dto.*;
import com.Travelrithm.dto.register.MemberRegisterInfo;
import com.Travelrithm.dto.register.MemberRequestDto;
import com.Travelrithm.repository.MemberRepository;
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

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public MemberResponseDto createOAuthUser(MemberRegisterInfo memberRegisterInfo){
        Member user = memberRepository.findByEmail(memberRegisterInfo.getEmail())
                .orElseGet(() -> {
                    Member newUser = Member.builder()
                            .socialId(memberRegisterInfo.getSocialId())
                            .socialType(memberRegisterInfo.getSocialType())
                            .name(memberRegisterInfo.getName())
                            .email(memberRegisterInfo.getEmail())
                            .nickname(memberRegisterInfo.getNickName())
                            .build();
                    return memberRepository.save(newUser);
                });

        return new MemberResponseDto(user);/*** 차후에 userNotfoundException에러를 던져야됨***/
    }
    public MemberResponseDto createUser(MemberRequestDto userRegisterInfo){
        Member user = memberRepository.findByEmail(userRegisterInfo.email())
                .orElseGet(() -> {
                    Member newUser = Member.builder()
                            .socialId(null)
                            .socialType(userRegisterInfo.socialType())
                            .name(userRegisterInfo.name())
                            .email(userRegisterInfo.email())
                            .role("ROLE_USER")
                            .password(bCryptPasswordEncoder.encode(userRegisterInfo.password()))
                            .nickname(userRegisterInfo.nickname())
                            .build();
                    return memberRepository.save(newUser);
                });

        return new MemberResponseDto(user);/*** 차후에 userNotfoundException에러를 던져야됨***/
    }


    @Transactional(readOnly = true)
    public MemberResponseDto findUser(Long id) {
        Member user = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new MemberResponseDto(user);
    }
    public MemberResponseDto findUser(String email){
        Member user= memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자 입니다. "));
        return new MemberResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllUsers() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::new)
                .toList();
    }

    public void deleteUser(Long id){
        memberRepository.deleteById(id);
    }

    public MemberResponseDto updateUser(Long userId, MemberRequestDto updatedUserDto){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다"));
        member.update(updatedUserDto);
        return new MemberResponseDto(member);
    }

    private void validateDuplicateEmail(Member user) {
        memberRepository.findByEmail(user.getEmail())
                .ifPresent(i-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }




}
