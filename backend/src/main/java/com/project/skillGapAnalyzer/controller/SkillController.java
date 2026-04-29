package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.CategoryResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.service.CategoryService;
import com.project.skillGapAnalyzer.service.RoleService;
import com.project.skillGapAnalyzer.service.SkillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
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
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        logger.debug("Fetching all categories");
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        logger.info("Fetched {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponseDTO>> getRoles(
            @RequestParam @NotBlank String category){
        List<RoleResponseDTO> roles = roleService.getRoles(category);
        logger.info("Fetched {} roles for category: {}", roles.size(), category);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/roles/{roleName}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(
            @PathVariable @NotBlank String roleName){
        RoleResponseDTO role = roleService.getRoleByName(roleName);
        logger.info("Fetched role: {}", roleName);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<String>>> getAllSkillsByCategory() {
        return ResponseEntity.ok(skillService.getAllSkillsByCategory());
    }

    @PostMapping("/analyze")
    public ResponseEntity<SkillAnalysisResponseDTO> analyzeSkills(
            @Valid @RequestBody SkillGapRequestDTO request){
        logger.info("Skill analysis started | role: {}, skills: {}, includeRepos: {}",
                request.getTargetRole(),
                request.getUserSkills().size(),
                request.isIncludeRepos());
        SkillAnalysisResponseDTO result =
                skillService.analyzeSkills(
                        request.getUserSkills(),
                        request.getTargetRole(),
                        request.getExperienceLevel(),
                        request.isIncludeRepos());
        logger.info("Skill analysis completed for role: {}", request.getTargetRole());
        return ResponseEntity.ok(result);
    }
}