package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.SaveAnalysisRequestDTO;
import com.project.skillGapAnalyzer.model.Analysis;
import com.project.skillGapAnalyzer.repository.AnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;

    public AnalysisService(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    public void saveAnalysis(SaveAnalysisRequestDTO request) {

        Analysis analysis = Analysis.builder()
                .userId(request.getUserId())
                .roleName(request.getTargetRole())
                .missingSkills(request.getMissingSkills())
                .createdAt(LocalDateTime.now())
                .build();

        analysisRepository.save(analysis);
    }
}
