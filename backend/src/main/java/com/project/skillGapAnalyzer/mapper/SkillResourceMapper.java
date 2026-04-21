package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.SkillResourceResponseDTO;
import com.project.skillGapAnalyzer.model.SkillResource;
import org.springframework.stereotype.Component;

@Component
public class SkillResourceMapper {

    public SkillResourceResponseDTO toDTO(SkillResource resource){
        return SkillResourceResponseDTO.builder()
                .id(resource.getId())
                .skill(resource.getSkill())
                .resources(resource.getResources())
                .build();
    }
}
