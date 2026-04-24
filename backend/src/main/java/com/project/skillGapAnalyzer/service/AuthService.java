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
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final static String TOKEN_TYPE = "Bearer";

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

        String username = StringNormalizer.normalize(dto.getUserName());
        String email = StringNormalizer.normalize(dto.getEmail());

        logger.debug("Signup attempt for username: {}", username);

        if (userRepository.existsByUserNameIgnoreCase(username)) {
            throw new BadRequestException("Username already exists.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Email already exists.");
        }

        User user = userMapper.toEntity(dto);
        user.setUserName(username);
        user.setEmail(email);
        user.setRole(UserRole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        logger.debug("User created successfully with id: {}", saved.getId());

        return userMapper.toDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO dto){

        String username = StringNormalizer.normalize(dto.getUserName());

        logger.debug("Login attempt for username: {}", username);

        User user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username/password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username/password");
        }

        String role = user.getRole().name();
        String token = jwtUtil.generateToken(username, role);

        logger.debug("Login successful for username: {}", username);

        return new LoginResponseDTO(token, TOKEN_TYPE, user.getRole(), user.getId());
    }
}