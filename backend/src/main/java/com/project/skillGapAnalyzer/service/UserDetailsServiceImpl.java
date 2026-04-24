package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String normalizedUsername = username == null ? null : username.trim().toLowerCase();

        logger.debug("Loading user details for authentication");

        User user = userRepository.findByUserNameIgnoreCase(normalizedUsername)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", normalizedUsername);
                    return new UsernameNotFoundException("User not found");
                });

        String role = user.getRole() != null
                ? user.getRole().name()
                : "ROLE_USER";

        logger.debug("User authenticated successfully: {}", normalizedUsername);
        logger.info("Assigned role: {}", role);
        return new org.springframework.security.core.userdetails.User(
                user.getUserName().toLowerCase(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
