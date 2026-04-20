package com.project.skillGapAnalyzer.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateDTO {

    private String roleName;
    private String category;
    private List<String> skills;

}
