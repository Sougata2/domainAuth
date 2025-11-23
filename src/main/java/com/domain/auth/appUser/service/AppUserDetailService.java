package com.domain.auth.appUser.service;

import com.domain.auth.appUser.details.AppUserDetails;
import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> entity = repository.findByEmail(username);
        if (entity.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new AppUserDetails(entity.get());
    }
}
