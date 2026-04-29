package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisDetailResponseDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.MessageResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.service.AnalysisService;
import com.project.skillGapAnalyzer.service.SkillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private final AnalysisService analysisService;
    private final SkillService skillService;

    public AnalysisController(AnalysisService analysisService,
                              SkillService skillService) {
        this.analysisService = analysisService;
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillAnalysisResponseDTO> analyzeAndSave(
            @Valid @RequestBody SkillGapRequestDTO request) {
        logger.info("Analyzing skills and saving result for role: {}", request.getTargetRole());
        logger.debug("User skills: {}", request.getUserSkills());
        SkillAnalysisResponseDTO result = skillService.analyzeSkills(
                request.getUserSkills(),
                request.getTargetRole(),
                request.getExperienceLevel(),
                request.isIncludeRepos()
        );
        analysisService.saveAnalysis(request, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<AnalysisResponseDTO>> getUserAnalysis(
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int size) {
        logger.info("Fetching analysis history | page: {}, size: {}", page, size);
        return ResponseEntity.ok(
                analysisService.getUserAnalysis(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDetailResponseDTO> getAnalysisById(@PathVariable String id) {

        logger.info("Fetching analysis with id: {}", id);
        return ResponseEntity.ok(
                analysisService.getAnalysisById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteAnalysis(@PathVariable String id) {
        analysisService.deleteAnalysis(id);
        return ResponseEntity.ok(new MessageResponseDTO("Analysis deleted"));
    }
}
