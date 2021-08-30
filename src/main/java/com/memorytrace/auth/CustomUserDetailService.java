package com.memorytrace.auth;

import com.memorytrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String snsKey) throws UsernameNotFoundException {

        return userRepository.findBySnsKey(snsKey)
            .orElseThrow(() -> new UsernameNotFoundException("[ "+ snsKey + "] 사용자를 찾을 수 없습니다."));
    }
}
