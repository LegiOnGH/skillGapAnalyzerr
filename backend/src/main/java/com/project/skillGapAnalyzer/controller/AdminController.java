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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/category")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO category) {
        logger.info("Admin creating category: {}", category.getName());
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    //create role
    @PostMapping("/role")
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO role) {

        logger.info("Creating role: {}", role.getRoleName());
        RoleResponseDTO created = roleService.createRole(role);
        return ResponseEntity.ok(created);
    }

    //update role
    @PatchMapping("/role/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable String id,
            @Valid @RequestBody RoleUpdateDTO dto) {

        logger.info("Updating role: {}", id);
        RoleResponseDTO role = roleService.updateRole(id, dto);
        return ResponseEntity.ok(role);
    }

    //delete role
    @DeleteMapping("/role/{id}")
    public ResponseEntity<MessageResponseDTO> deleteRole(@PathVariable String id) {

        logger.info("Deleting role: {}", id);

        roleService.deleteRole(id);

        return ResponseEntity.ok(
                new MessageResponseDTO("Role deleted successfully"));
    }

    // 🔹 Add Skill Resource
    @PostMapping("/resource")
    public ResponseEntity<SkillResourceResponseDTO> addResource(@Valid @RequestBody SkillResourceRequestDTO resource) {
        logger.info("Admin adding resource for skill: {}", resource.getSkill());
        return ResponseEntity.ok(skillResourceService.addResource(resource));
    }

    @PatchMapping("/resource/{id}")
    public ResponseEntity<SkillResourceResponseDTO> updateResource(
            @PathVariable String id,
            @RequestBody SkillResourceUpdateDTO resource) {

        return ResponseEntity.ok(skillResourceService.updateResource(id, resource));
    }

    @DeleteMapping("/resource/{id}")
    public ResponseEntity<MessageResponseDTO> deleteResource(@PathVariable String id, @RequestBody SkillResourceDeleteDTO dto) {

        skillResourceService.deleteResource(id, dto.getResourceToDelete());

        return ResponseEntity.ok(
                 new MessageResponseDTO("Resource deleted"));
    }
}
