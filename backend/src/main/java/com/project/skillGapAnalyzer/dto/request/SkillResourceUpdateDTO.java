package com.project.skillGapAnalyzer.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SkillResourceUpdateDTO {

    private String skill;
    private List<String> resources;
}
