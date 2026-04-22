package com.project.skillGapAnalyzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillAnalysisResponseDTO {

    private List<String> matchedSkills;
    private List<String> missingSkills;
    private int progress;
    private Map<String, List<String>> resourcesBySkill;
    private Map<String, List<RepoDTO>> reposBySkill;

}
