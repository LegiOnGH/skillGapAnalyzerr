package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RepoDTO;
import com.project.skillGapAnalyzer.model.Role;
import com.project.skillGapAnalyzer.repository.SkillResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    private final RoleService roleService;
    private final SkillResourceRepository skillResourceRepository;
    private final GitHubService gitHubService;

    public SkillService(RoleService roleService,
                        SkillResourceRepository skillResourceRepository,
                        GitHubService gitHubService) {
        this.roleService = roleService;
        this.skillResourceRepository = skillResourceRepository;
        this.gitHubService = gitHubService;
    }

    public SkillAnalysisResponseDTO analyzeSkills(
            List<String> userSkills,
            String targetRole,
            String experienceLevel,
            boolean includeRepos) {

        logger.info("Starting skill analysis for role: {}", targetRole);

        Role role = roleService.getRoleByName(targetRole);

        List<String> targetSkills = role.getSkills();

        Set<String> userSkillSet = normalizeSkills(userSkills);

        List<String> matched = getMatchedSkills(targetSkills, userSkillSet);
        List<String> missing = getMissingSkills(targetSkills, userSkillSet);

        int progress = calculateProgress(matched.size(), targetSkills.size());

        Map<String, List<String>> resources = getResourcesForMissingSkills(missing);

        Map<String, List<RepoDTO>> repos = includeRepos
                ? gitHubService.getReposForSkills(missing, experienceLevel)
                : Collections.emptyMap();

        logger.info("Skill analysis completed for role: {}", targetRole);

        return new SkillAnalysisResponseDTO(
                matched,
                missing,
                progress,
                resources,
                repos
        );
    }

    private Set<String> normalizeSkills(List<String> skills) {
        return skills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private List<String> getMatchedSkills(List<String> targetSkills, Set<String> userSkills) {
        return targetSkills.stream()
                .filter(skill -> userSkills.contains(skill.toLowerCase()))
                .toList();
    }

    private List<String> getMissingSkills(List<String> targetSkills, Set<String> userSkills) {
        return targetSkills.stream()
                .filter(skill -> !userSkills.contains(skill.toLowerCase()))
                .toList();
    }

    private int calculateProgress(int matched, int total) {
        if (total == 0) return 0;
        return (int) (((double) matched / total) * 100);
    }

    public Map<String, List<String>> getResourcesForMissingSkills(List<String> missing) {

        Map<String, List<String>> resources = new HashMap<>();

        for (String skill : missing) {
            skillResourceRepository.findBySkillIgnoreCase(skill)
                    .ifPresent(resource -> resources.put(skill, resource.getResources()));
        }

        return resources;
    }
}
