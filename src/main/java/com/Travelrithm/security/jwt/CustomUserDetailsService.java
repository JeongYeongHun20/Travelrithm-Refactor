package com.Travelrithm.security.jwt;

import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
        if (userData != null) {
            log.info(userData.getEmail()+" "+userData.getName()+" "+userData.getPassword());
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
