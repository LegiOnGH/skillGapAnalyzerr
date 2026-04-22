package com.project.skillGapAnalyzer.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResourceDeleteDTO {
    private String resourceToDelete;
}
