package com.project.skillGapAnalyzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class AnalysisResponseDTO {

    private String id;
    private String targetRole;
    private List<String> missingSkills;
    private int progress;
    private Instant createdAt;
}
