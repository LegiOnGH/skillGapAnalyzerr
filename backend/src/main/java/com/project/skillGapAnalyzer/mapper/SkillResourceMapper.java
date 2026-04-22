package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.SkillResourceResponseDTO;
import com.project.skillGapAnalyzer.model.SkillResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillResourceMapper {

    public SkillResourceResponseDTO toDTO(SkillResource resource){
        if (resource == null) return null;

        return SkillResourceResponseDTO.builder()
                .id(resource.getId())
                .skill(resource.getSkill().trim())
                .resources(resource.getResources() != null
                        ? List.copyOf(resource.getResources())
                        : List.of())
                .build();
    }
}
