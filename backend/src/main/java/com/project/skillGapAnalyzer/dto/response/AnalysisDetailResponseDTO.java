package com.project.skillGapAnalyzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDetailResponseDTO {
    private String id;
    private String targetRole;
    private List<String> userSkills;
    private List<String> missingSkills;
    private int progress;
    private Instant createdAt;
}
