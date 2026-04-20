package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.RoleRequestDTO;
import com.project.skillGapAnalyzer.dto.request.RoleUpdateDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.model.Role;
import com.project.skillGapAnalyzer.repository.CategoryRepository;
import com.project.skillGapAnalyzer.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    public RoleService(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    public Role createRole(RoleRequestDTO dto) {

        logger.info("Creating role: {}", dto.getRoleName());

        String roleName = dto.getRoleName().trim();
        String category = dto.getCategory().trim().toLowerCase();

        if (roleRepository.findByRoleNameIgnoreCase(roleName).isPresent()) {
            throw new BadRequestException("Role already exists: " + roleName);
        }

        if (!categoryRepository.existsByNameIgnoreCase(category)) {
            throw new BadRequestException("Invalid category: " + category);
        }

        List<String> normalizedSkills = dto.getSkills()
                .stream()
                .map(skill -> skill.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (normalizedSkills.isEmpty()) {
            throw new BadRequestException("Role must have at least one valid skill");
        }

        Role role = Role.builder()
                .roleName(roleName)
                .category(category)
                .skills(normalizedSkills)
                .build();

        Role saved = roleRepository.save(role);

        logger.info("Role created successfully: {}", saved.getId());

        return saved;
    }

    public Role updateRole(String id, RoleUpdateDTO dto) {

        logger.info("Updating role: {}", id);

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        if (dto.getRoleName() != null && !dto.getRoleName().trim().isEmpty()) {
            String newName = dto.getRoleName().trim();

            roleRepository.findByRoleNameIgnoreCase(newName)
                    .ifPresent(role -> {
                        if (!role.getId().equals(id)) {
                            throw new BadRequestException("Role already exists: " + newName);
                        }
                    });

            existing.setRoleName(newName);
        }

        if (dto.getCategory() != null && !dto.getCategory().trim().isEmpty()) {
            String category = dto.getCategory().trim().toLowerCase();

            if (!categoryRepository.existsByNameIgnoreCase(category)) {
                throw new BadRequestException("Invalid category: " + category);
            }

            existing.setCategory(category);
        }

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {

            List<String> normalizedSkills = dto.getSkills()
                    .stream()
                    .map(skill -> skill.trim().toLowerCase())
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            if (normalizedSkills.isEmpty()) {
                throw new BadRequestException("Skills cannot be empty");
            }

            existing.setSkills(normalizedSkills);
        }

        Role updated = roleRepository.save(existing);

        logger.info("Role updated successfully: {}", id);

        return updated;
    }

    public void deleteRole(String id) {

        logger.info("Deleting role: {}", id);

        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }

        roleRepository.deleteById(id);

        logger.info("Role deleted successfully: {}", id);
    }

    public List<Role> getRoles(String category){

        logger.info("Fetching roles for category: {}", category);

        List<Role> roles = roleRepository.findByCategoryIgnoreCase(category);

        if (roles.isEmpty()) {
            logger.warn("No roles found for category: {}", category);
        } else {
            logger.info("Fetched {} roles for category: {}", roles.size(), category);
        }

        return roles;
    }

    public Role getRoleByName(String roleName){

        logger.info("Fetching role by name: {}", roleName);

        return roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> {
                    logger.warn("Role not found: {}", roleName);
                    return new ResourceNotFoundException("Role not found: " + roleName);
                });
    }
}