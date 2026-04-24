package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.AnalysisResponseDTO;
import com.project.skillGapAnalyzer.model.Analysis;
import org.springframework.stereotype.Component;

@Component
public class AnalysisMapper {
    public AnalysisResponseDTO toDTO(Analysis analysis){
        if(analysis == null) return null;

        return AnalysisResponseDTO.builder()
                .id(analysis.getId())
                .targetRole(analysis.getTargetRole())
                .progress(analysis.getProgress())
                .createdAt(analysis.getCreatedAt())
                .build();
    }
}
