package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.*;
import com.project.skillGapAnalyzer.dto.response.CategoryResponseDTO;
import com.project.skillGapAnalyzer.dto.response.MessageResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillResourceResponseDTO;
import com.project.skillGapAnalyzer.service.CategoryService;
import com.project.skillGapAnalyzer.service.RoleService;
import com.project.skillGapAnalyzer.service.SkillResourceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final CategoryService categoryService;
    private final RoleService roleService;
    private final SkillResourceService skillResourceService;

    public AdminController(CategoryService categoryService,
                           RoleService roleService,
                           SkillResourceService skillResourceService) {
        this.categoryService = categoryService;
        this.roleService = roleService;
        this.skillResourceService = skillResourceService;
    }

    // 🔹 Create Category
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO category) {
        logger.info("Admin creating category: {}", category.getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryResponseDTO response = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //create role
    @PostMapping("/roles")
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO role) {
        logger.info("Admin creating role: {}", role.getRoleName());
        RoleResponseDTO created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //update role
    @PatchMapping("/roles/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable String id,
            @Valid @RequestBody RoleUpdateDTO dto) {
        logger.info("Admin updating role: {}", id);
        RoleResponseDTO updated = roleService.updateRole(id, dto);
        return ResponseEntity.ok(updated);
    }

    //delete role
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<MessageResponseDTO> deleteRole(@PathVariable String id) {
        logger.info("Admin deleting role: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.ok(
                new MessageResponseDTO("Role deleted successfully"));
    }

    @GetMapping("/resources")
    public ResponseEntity<List<SkillResourceResponseDTO>> getAllResources() {
        logger.debug("Admin fetching all resources");
        return ResponseEntity.ok(skillResourceService.getAllResources());
    }

    @PostMapping("/resources")
    public ResponseEntity<SkillResourceResponseDTO> addResource(@Valid @RequestBody SkillResourceRequestDTO resource) {
        logger.info("Admin adding resource for skill: {}", resource.getSkill());
        SkillResourceResponseDTO response =
                skillResourceService.addResource(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/resources/{id}")
    public ResponseEntity<SkillResourceResponseDTO> updateResource(
            @PathVariable String id,
            @Valid @RequestBody SkillResourceUpdateDTO resource) {
        logger.debug("Updating resource with id: {}", id);
        SkillResourceResponseDTO updated =
                skillResourceService.updateResource(id, resource);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/resources/{id}")
    public ResponseEntity<MessageResponseDTO> deleteResource(@PathVariable String id, @RequestParam String resource) {
        logger.info("Admin deleting resource '{}' for id: {}", resource, id);
        skillResourceService.deleteResource(id, resource);
        return ResponseEntity.ok(
                 new MessageResponseDTO("Resource deleted"));
    }
}
