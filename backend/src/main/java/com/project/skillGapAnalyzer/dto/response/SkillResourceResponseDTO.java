package com.project.skillGapAnalyzer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkillResourceResponseDTO {
    private String id;
    private String skill;
    private List<String> resources;
}
