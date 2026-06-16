package com.hsms.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hsms.auth_service.entity.AuthUser;
import com.hsms.auth_service.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {

        AuthUser user = authUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build();
    }
}