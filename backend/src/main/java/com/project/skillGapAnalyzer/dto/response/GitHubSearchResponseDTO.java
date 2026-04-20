package com.project.skillGapAnalyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.skillGapAnalyzer.dto.github.GitHubRepoItemDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubSearchResponseDTO {

    private List<GitHubRepoItemDTO> items;

}
