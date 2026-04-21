package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponseDTO toDTO(Role role){
        return RoleResponseDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .category(role.getCategory())
                .skills(role.getSkills())
                .build();
    }
}
