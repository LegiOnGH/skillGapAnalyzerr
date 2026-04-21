package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.SaveAnalysisRequestDTO;
import com.project.skillGapAnalyzer.dto.response.MessageResponseDTO;
import com.project.skillGapAnalyzer.service.AnalysisService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/save")
    public ResponseEntity<MessageResponseDTO> saveAnalysis(
            @Valid @RequestBody SaveAnalysisRequestDTO request) {

        logger.info("Saving analysis for user: {}", request.getUserId());
        analysisService.saveAnalysis(request);

        return ResponseEntity.ok(
                new MessageResponseDTO("Analysis saved successfully")
        );
    }
}
