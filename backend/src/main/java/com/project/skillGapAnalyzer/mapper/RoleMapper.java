package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleMapper {

    public RoleResponseDTO toDTO(Role role){
        if (role == null) return null;

        return RoleResponseDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName().trim())
                .category(role.getCategory().trim())
                .skills(role.getSkills() != null
                        ? List.copyOf(role.getSkills())
                        : List.of())
                .build();
    }
}
