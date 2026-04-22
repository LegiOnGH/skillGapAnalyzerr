package com.project.skillGapAnalyzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {

    private String id;
    private String roleName;
    private String category;
    private List<String> skills;
}
