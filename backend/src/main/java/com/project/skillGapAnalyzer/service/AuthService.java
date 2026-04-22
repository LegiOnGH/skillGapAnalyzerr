package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.LoginRequestDTO;
import com.project.skillGapAnalyzer.dto.request.SignupRequestDTO;
import com.project.skillGapAnalyzer.dto.response.LoginResponseDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.enums.UserRole;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.mapper.UserMapper;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.UserRepository;
import com.project.skillGapAnalyzer.security.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDTO signup(SignupRequestDTO dto) {

        logger.info("Signup attempt for username: {}", dto.getUserName());

        if (userRepository.findByUserName(dto.getUserName()).isPresent()) {
            logger.warn("Signup failed - username already exists: {}", dto.getUserName());
            throw new BadRequestException("Username already exists.");
        }

        User user = userMapper.toEntity(dto);
        user.setRole(UserRole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        logger.info("User created successfully with id: {}", saved.getId());

        return userMapper.toDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO dto){

        logger.info("Login attempt for username: {}", dto.getUserName());

        User user = userRepository.findByUserName(dto.getUserName())
                .orElseThrow(() -> {
                    logger.warn("Login failed - user not found: {}", dto.getUserName());
                    return new BadCredentialsException("Invalid username/password");
                });

        boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        if (!isMatch) {
            logger.warn("Login failed - invalid password for username: {}", dto.getUserName());
            throw new BadCredentialsException("Invalid username/password");
        }

        String role = user.getRole().name();
        String token = jwtUtil.generateToken(user.getUserName(), role);

        logger.info("Login successful for username: {}", dto.getUserName());

        return new LoginResponseDTO(token, "Bearer", user.getRole(), user.getId());
    }
}