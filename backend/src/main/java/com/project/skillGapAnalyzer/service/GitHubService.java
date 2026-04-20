package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.GitHubSearchResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RepoDTO;
import com.project.skillGapAnalyzer.exceptions.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private static final int MAX_REPOS = 5;

    private final RestTemplate restTemplate;

    @Value("${github.url}")
    private String github;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepoDTO> fetchRepositories(String query){

        logger.info("Fetching repositories for query: {}", query);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = github + encodedQuery;

        GitHubSearchResponseDTO response;

        try {
            response = restTemplate.getForObject(url, GitHubSearchResponseDTO.class);
        } catch (Exception e) {
            logger.error("Error calling GitHub API for query: {}", query, e);
            throw new ExternalServiceException("Failed to fetch repositories from GitHub");
        }

        if (response == null || response.getItems() == null) {
            logger.error("Invalid response from GitHub API for query: {}", query);
            throw new ExternalServiceException("Invalid response from GitHub API");
        }

        return response.getItems().stream()
                .limit(MAX_REPOS)
                .map(item -> {
                    RepoDTO dto = new RepoDTO();
                    dto.setName(item.getName());
                    dto.setUrl(item.getHtmlUrl());
                    dto.setDescription(item.getDescription());
                    return dto;
                })
                .toList();
    }

    public String buildQuery(String skill, String experienceLevel){
        return switch (experienceLevel.toLowerCase()) {
            case "beginner" -> skill + " beginner project";
            case "intermediate" -> skill + " intermediate project";
            case "advanced" -> skill + " advanced project";
            default -> skill + " project";
        };
    }

    public Map<String, List<RepoDTO>> getReposForSkills(List<String> skills, String experienceLevel){

        logger.info("Fetching repositories for {} skills at {} level", skills.size(), experienceLevel);

        Map<String, List<RepoDTO>> result = new HashMap<>();

        skills.stream()
                .limit(3)
                .forEach(skill -> {
                    String query = buildQuery(skill, experienceLevel);
                    List<RepoDTO> repos = fetchRepositories(query);
                    result.put(skill, repos);
                });

        logger.info("Repository fetch completed");

        return result;
    }
}
