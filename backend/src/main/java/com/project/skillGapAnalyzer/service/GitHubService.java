package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.GitHubSearchResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RepoDTO;
import com.project.skillGapAnalyzer.dto.response.RepoResponseDTO;
import com.project.skillGapAnalyzer.enums.Experience;
import com.project.skillGapAnalyzer.exceptions.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private static final int MAX_REPOS = 5;

    private final RestTemplate restTemplate;

    @Value("${github.url}")
    private String githubURL;

    @Value("${github.token}")
    private String githubToken;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepoDTO> fetchRepositories(String query){

        logger.debug("Fetching repositories for query: {}", query);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = githubURL + encodedQuery;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        if(githubToken != null && !githubToken.isBlank()){
            headers.setBearerAuth(githubToken);
        } else {
            logger.warn("GitHub token not configured. Using unauthenticated requests (rate limits may apply)");
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubSearchResponseDTO> response;

        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GitHubSearchResponseDTO.class
            );
        } catch (RestClientException e) {
            logger.error("Error calling GitHub API for query: {}", query, e);
            throw new ExternalServiceException("Failed to fetch repositories from GitHub");
        }

        GitHubSearchResponseDTO body = response.getBody();

        if (body == null || body.getItems() == null) {
            logger.error("Invalid response from GitHub API for query: {}", query);
            throw new ExternalServiceException("Invalid response from GitHub API");
        }


        String remaining = response.getHeaders().getFirst("X-RateLimit-Remaining");
        logger.debug("GitHub rate limit remaining: {}", remaining);

        return body.getItems().stream()
                .limit(MAX_REPOS)
                .map(item -> RepoDTO.builder()
                        .name(item.getName())
                        .url(item.getHtmlUrl())
                        .description(
                                item.getDescription() != null ? item.getDescription() : "No description"
                        )
                        .stars(item.getStars())
                        .build())
                .toList();
    }

    public String buildQuery(String skill, Experience experienceLevel){
        return switch (experienceLevel) {
            case BEGINNER -> skill + " beginner project";
            case INTERMEDIATE -> skill + " intermediate project";
            case ADVANCED -> skill + " advanced project";
        };
    }

    public RepoResponseDTO getReposForSkills(List<String> skills, Experience experienceLevel){

        if (skills == null || skills.isEmpty()) {
            logger.warn("No skills provided for repository fetch");
            return new RepoResponseDTO(Collections.emptyMap());
        }

        logger.debug("Fetching repositories for {} skills at {} level", skills.size(), experienceLevel);

        Map<String, List<RepoDTO>> result = new HashMap<>();

        skills.stream()
                .limit(3)
                .forEach(skill -> {
                    String query = buildQuery(skill, experienceLevel);
                    List<RepoDTO> repos = fetchRepositories(query);
                    result.put(skill, repos);
                });

        logger.debug("Repository fetch completed");

        return new RepoResponseDTO(result);
    }
}
