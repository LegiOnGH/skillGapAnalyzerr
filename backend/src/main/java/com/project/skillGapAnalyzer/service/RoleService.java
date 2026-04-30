package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.RoleRequestDTO;
import com.project.skillGapAnalyzer.dto.request.RoleUpdateDTO;
import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.mapper.RoleMapper;
import com.project.skillGapAnalyzer.model.Role;
import com.project.skillGapAnalyzer.repository.CategoryRepository;
import com.project.skillGapAnalyzer.repository.RoleRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, CategoryRepository categoryRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.roleMapper = roleMapper;
    }

    @CacheEvict(value = {"roles", "allSkills"}, allEntries = true)
    public RoleResponseDTO createRole(RoleRequestDTO dto) {

        logger.debug("Creating role: {}", dto.getRoleName());

        String roleName = StringNormalizer.normalizePreserveCase(dto.getRoleName());
        String category = StringNormalizer.normalizePreserveCase(dto.getCategory());

        if (roleRepository.findByRoleNameIgnoreCase(roleName).isPresent()) {
            throw new BadRequestException("Role already exists: " + roleName);
        }

        if (!categoryRepository.existsByNameIgnoreCase(category)) {
            throw new BadRequestException("Invalid category: " + category);
        }

        List<String> normalizedSkills = StringNormalizer.normalizeListPreserveCase(dto.getSkills());

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

        return roleMapper.toDTO(saved);
    }
    @CacheEvict(value = {"roles", "allSkills"}, allEntries = true)
    public RoleResponseDTO updateRole(String id, RoleUpdateDTO dto) {

        logger.debug("Updating role: {}", id);

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        String newName = StringNormalizer.normalizePreserveCase(dto.getRoleName());
        if (newName != null && !newName.isEmpty()) {
            roleRepository.findByRoleNameIgnoreCase(newName)
                    .filter(role -> !role.getId().equals(id))
                    .ifPresent(role -> {
                        throw new BadRequestException("Role already exists: " + newName);
                    });
            existing.setRoleName(newName);
        }

        String category = StringNormalizer.normalizePreserveCase(dto.getCategory());
        if(category != null && !category.isEmpty()){
            if (!categoryRepository.existsByNameIgnoreCase(category)) {
                throw new BadRequestException("Invalid category: " + category);
            }
            existing.setCategory(category);
        }

        if (dto.getSkills() != null) {
            List<String> normalizedSkills = StringNormalizer.normalizeListPreserveCase(dto.getSkills());
            if (normalizedSkills.isEmpty()) {
                throw new BadRequestException("Skills cannot be empty");
            }
            existing.setSkills(normalizedSkills);
        }

        Role updated = roleRepository.save(existing);

        logger.info("Role updated successfully: {}", id);

        return roleMapper.toDTO(updated);
    }

    @CacheEvict(value = {"roles", "allSkills"}, allEntries = true)
    public void deleteRole(String id) {

        logger.debug("Deleting role: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        roleRepository.delete(role);

        logger.info("Role deleted successfully: {}", id);
    }

    @Cacheable(value = "roles", key = "#category")
    public List<RoleResponseDTO> getRoles(String category){

        logger.debug("Fetching roles for category: {}", category);
        String normalizedCategory = StringNormalizer.normalize(category);
        List<Role> roles = roleRepository.findByCategoryIgnoreCase(normalizedCategory);

        if (roles.isEmpty()) {
            logger.info("No roles found for category: {}", normalizedCategory);
        } else {
            logger.debug("Fetched {} roles for category: {}", roles.size(), normalizedCategory);
        }

        return roles.stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    public RoleResponseDTO getRoleByName(String roleName){

        logger.debug("Fetching role by name: {}", roleName);

        String normalizedName = StringNormalizer.normalizePreserveCase(roleName);
        Role role = roleRepository.findByRoleNameIgnoreCase(normalizedName)
                .orElseThrow(() -> {
                    logger.warn("Role not found: {}", normalizedName);
                    return new ResourceNotFoundException("Role not found: " + normalizedName);
                });

        return roleMapper.toDTO(role);
    }
}