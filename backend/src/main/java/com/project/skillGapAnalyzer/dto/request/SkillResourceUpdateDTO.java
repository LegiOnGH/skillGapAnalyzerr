package com.project.skillGapAnalyzer.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SkillResourceUpdateDTO {
    private String oldResource;
    private String newResource;
}
