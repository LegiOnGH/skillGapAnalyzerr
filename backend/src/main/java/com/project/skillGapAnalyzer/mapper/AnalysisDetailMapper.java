package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.AnalysisDetailResponseDTO;
import com.project.skillGapAnalyzer.model.Analysis;
import org.springframework.stereotype.Component;


@Component
public class AnalysisDetailMapper {
    public AnalysisDetailResponseDTO toDTO(Analysis analysis){

        if(analysis == null) return null;

        return AnalysisDetailResponseDTO.builder()
                .id(analysis.getId())
                .targetRole(analysis.getTargetRole())
                .userSkills(analysis.getUserSkills())
                .missingSkills(analysis.getMissingSkills())
                .progress(analysis.getProgress())
                .createdAt(analysis.getCreatedAt())
                .build();
    }
}
