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
    public void updateUserRole(String userId, UserRole newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userRepository.findByUserNameIgnoreCase(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        if (user.getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot change your own role");
        }

        if (user.getRole() == UserRole.ROLE_SUPER_ADMIN) {
            long superAdminCount = userRepository.countByRole(UserRole.ROLE_SUPER_ADMIN);
            if (superAdminCount <= 1) {
                throw new BadRequestException("Cannot demote the last Super Admin");
            }
        }

        user.setRole(newRole);
        userRepository.save(user);
        logger.info("User {} role updated to {}", userId, newRole);
    }

    @Transactional
    public void deleteUser(String userId) {

        logger.debug("Delete request received for userId: {}", userId);

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found for deletion: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Unauthenticated request");
        }

        String currentUserName = StringNormalizer.normalize(authentication.getName());

        User currentUser = userRepository.findByUserNameIgnoreCase(currentUserName)
                .orElseThrow(() -> {
                    logger.error("Authenticated user not found in DB");
                    return new ResourceNotFoundException("Authenticated user not found");
                });

        if (currentUser.getId().equals(userToDelete.getId())) {
            logger.warn("Self-deletion attempt blocked for userId: {}", userId);
            throw new BadRequestException("User cannot delete their own account");
        }

        if (userToDelete.getRole() == UserRole.ROLE_SUPER_ADMIN) {

            long superAdminCount = userRepository.countByRole(UserRole.ROLE_SUPER_ADMIN);

            if (superAdminCount <= 1) {
                logger.warn("Attempt to delete last SUPER_ADMIN: {}", userId);
                throw new BadRequestException("Cannot delete the last Super Admin");
            }
        }

        userRepository.delete(userToDelete);

        logger.info("User deleted successfully: {}", userId);
    }

    public Page<UserResponseDTO> getUsers(int page, int size, String search, UserRole role) {

        logger.debug("Fetching users | page: {}, size: {}, search: {}, role: {}",
                page, size, search, role);

        Pageable pageable = PageRequest.of(page, size, Sort.by("userName").ascending());

        String searchText = (search != null)
                ? StringNormalizer.normalize(search)
                : null;

        Page<User> users;

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

        logger.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        logger.info("User fetched successfully: {}", id);

        return userMapper.toDTO(user);
    }
}