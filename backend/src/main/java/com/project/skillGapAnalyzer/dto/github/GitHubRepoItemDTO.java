package com.project.skillGapAnalyzer.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepoItemDTO {
    private String name;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String description;
}
