package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.RepoRequestDTO;
import com.project.skillGapAnalyzer.dto.response.RepoResponseDTO;
import com.project.skillGapAnalyzer.service.GitHubService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repo")
public class RepoController {

    private static final Logger logger = LoggerFactory.getLogger(RepoController.class);

    private final GitHubService gitHubService;

    public RepoController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @PostMapping("/recommend")
    public ResponseEntity<RepoResponseDTO> recommendRepos(
            @Valid @RequestBody RepoRequestDTO request) {

        logger.info("Repo recommendation requested for skills: {}", request.getSkills());

        RepoResponseDTO repos =
                gitHubService.getReposForSkills(
                        request.getSkills(),
                        request.getExperienceLevel().name()
                );

        logger.info("Repo recommendation completed");

        return ResponseEntity.ok(repos);
    }
}
