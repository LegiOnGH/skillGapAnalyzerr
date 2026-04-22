package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.enums.UserRole;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void promoteUser(String userId){

        logger.info("Promoting user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId)
                );

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            throw new BadRequestException("User is already an ADMIN");
        }

        if (user.getRole() == UserRole.ROLE_SUPER_ADMIN) {
            throw new BadRequestException("Cannot modify SUPER_ADMIN role");
        }

        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);

        logger.info("User promoted successfully: {}", userId);
    }

    public void deleteUser(String userId) {

        logger.info("Delete request received for userId: {}", userId);

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId)
                );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByUserName(currentUserName)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found")
                );

        if (currentUser.getId().equals(userToDelete.getId())) {
            throw new BadRequestException("Super Admin cannot delete himself");
        }

        if (userToDelete.getRole() == UserRole.ROLE_SUPER_ADMIN) {

            long superAdminCount = userRepository.countByRole(UserRole.ROLE_SUPER_ADMIN);

            if (superAdminCount <= 1) {
                throw new BadRequestException("Cannot delete the last Super Admin");
            }
        }

        userRepository.deleteById(userId);

        logger.info("User deleted successfully: {}", userId);
    }
}