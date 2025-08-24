package com.nagi.e_commerce_spring.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nagi.e_commerce_spring.model.Users;
import com.nagi.e_commerce_spring.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Users user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                List<GrantedAuthority> authorities = List
                                .of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                                authorities);
        }
}
