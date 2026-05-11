package com.Travelrithm.security.jwt;

import com.Travelrithm.domain.Member;
import com.Travelrithm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member userData = memberRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
        if (userData != null) {
            log.info(userData.getEmail()+" "+userData.getName()+" "+userData.getPassword());
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
