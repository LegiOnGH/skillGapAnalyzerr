package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.SkillGapRequestDTO;
import com.project.skillGapAnalyzer.dto.response.AnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.service.AnalysisService;
import com.project.skillGapAnalyzer.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

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
    public ResponseEntity<List<AnalysisResponseDTO>> getUserAnalysis() {

        return ResponseEntity.ok(
                analysisService.getUserAnalysis()
        );
    }
}
