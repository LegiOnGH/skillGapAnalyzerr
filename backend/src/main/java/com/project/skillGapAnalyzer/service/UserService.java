package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.enums.UserRole;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.mapper.UserMapper;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.UserRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void promoteUser(String userId){

        logger.info("Promote request received for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found for promotion: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            logger.warn("User already ADMIN: {}", userId);
            throw new BadRequestException("User is already an ADMIN");
        }

        if (user.getRole() == UserRole.ROLE_SUPER_ADMIN) {
            logger.warn("Attempt to modify SUPER_ADMIN: {}", userId);
            throw new BadRequestException("Cannot modify SUPER_ADMIN role");
        }

        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);

        logger.info("User promoted successfully: {}", userId);
    }

    @Transactional
    public void deleteUser(String userId) {

        logger.info("Delete request received for userId: {}", userId);

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found for deletion: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByUserName(currentUserName)
                .orElseThrow(() -> {
                    logger.error("Authenticated user not found in DB: {}", currentUserName);
                    return new ResourceNotFoundException("Authenticated user not found");
                });

        if (currentUser.getId().equals(userToDelete.getId())) {
            logger.warn("Self-deletion attempt blocked for userId: {}", userId);
            throw new BadRequestException("Super Admin cannot delete himself");
        }

        if (userToDelete.getRole() == UserRole.ROLE_SUPER_ADMIN) {

            long superAdminCount = userRepository.countByRole(UserRole.ROLE_SUPER_ADMIN);

            if (superAdminCount <= 1) {
                logger.warn("Attempt to delete last SUPER_ADMIN: {}", userId);
                throw new BadRequestException("Cannot delete the last Super Admin");
            }
        }

        userRepository.deleteById(userId);

        logger.info("User deleted successfully: {}", userId);
    }

    public Page<UserResponseDTO> getUsers(int page, int size, String search, UserRole role) {

        logger.info("Fetching users | page: {}, size: {}, search: {}, role: {}",
                page, size, search, role);

        Pageable pageable = PageRequest.of(page, size, Sort.by("userName").ascending());

        Page<User> users;

        String searchText = (search != null)
                ? StringNormalizer.normalize(search)
                : null;

        if (searchText != null && role != null) {
            users = userRepository.findByUserNameContainingIgnoreCaseAndRole(searchText, role, pageable);
        } else if (searchText != null) {
            users = userRepository.findByUserNameContainingIgnoreCase(searchText, pageable);
        } else if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        logger.info("Fetched {} users (page {})", users.getNumberOfElements(), page);

        return users.map(userMapper::toDTO);
    }

    public UserResponseDTO getUserById(String id) {

        logger.info("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        logger.info("User fetched successfully: {}", id);

        return userMapper.toDTO(user);
    }
}