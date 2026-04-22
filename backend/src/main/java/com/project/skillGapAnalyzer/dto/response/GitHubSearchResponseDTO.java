package com.project.skillGapAnalyzer.dto.response;

import com.project.skillGapAnalyzer.dto.github.GitHubRepoItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class GitHubSearchResponseDTO {

    private List<GitHubRepoItemDTO> items;

}
