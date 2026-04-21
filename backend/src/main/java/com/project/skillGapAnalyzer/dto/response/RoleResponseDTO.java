package com.project.skillGapAnalyzer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleResponseDTO {

    private String id;
    private String roleName;
    private String category;
    private List<String> skills;
}
