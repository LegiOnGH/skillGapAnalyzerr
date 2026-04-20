package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.model.Category;
import com.project.skillGapAnalyzer.model.Role;
import com.project.skillGapAnalyzer.service.CategoryService;
import com.project.skillGapAnalyzer.service.RoleService;
import com.project.skillGapAnalyzer.service.SkillService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    private final CategoryService categoryService;
    private final RoleService roleService;
    private final SkillService skillService;

    public SkillController(CategoryService categoryService, RoleService roleService, SkillService skillService) {
        this.categoryService = categoryService;
        this.roleService = roleService;
        this.skillService = skillService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        logger.info("Fetching all categories");

        List<Category> categories = categoryService.getAllCategories();

        logger.info("Fetched {} categories", categories.size());

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(@RequestParam String category){
        logger.info("Fetching roles for category: {}", category);

        List<Role> roles = roleService.getRoles(category);

        logger.info("Fetched {} roles for category: {}", roles.size(), category);

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String roleName){
        logger.info("Fetching role by name: {}", roleName);

        Role role = roleService.getRoleByName(roleName);

        logger.info("Role found: {}", roleName);

        return ResponseEntity.ok(role);
    }

    @PostMapping("/analyze")
    public ResponseEntity<SkillAnalysisResponseDTO> analyzeSkills(
            @Valid @RequestBody SkillGapRequestDTO request){

        logger.info("Skill analysis started for role: {}",
                request.getTargetRole());

        String experienceLevel = request.getExperienceLevel() != null ?
                request.getExperienceLevel().name().toLowerCase()
                : "beginner";

        SkillAnalysisResponseDTO result =
                skillService.analyzeSkills(request.getUserSkills(),
                        request.getTargetRole(),
                        experienceLevel,
                        request.isIncludeRepos());

        logger.info("Skill analysis completed for role: {}", request.getTargetRole());

        return ResponseEntity.ok(result);
    }
}