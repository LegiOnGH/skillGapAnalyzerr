package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RepoDTO;
import com.project.skillGapAnalyzer.enums.Experience;
import com.project.skillGapAnalyzer.repository.SkillResourceRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

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
            Experience experienceLevel,
            boolean includeRepos) {

        logger.info("Starting skill analysis for role: {}", targetRole);

        RoleResponseDTO role = roleService.getRoleByName(targetRole);

        List<String> targetSkills = role.getSkills().stream()
                .map(StringNormalizer::normalize)
                .toList();

        Set<String> userSkillSet = userSkills == null
                ? Collections.emptySet()
                : StringNormalizer.normalizeSet(userSkills);

        List<String> matched = getMatchedSkills(targetSkills, userSkillSet);
        List<String> missing = getMissingSkills(targetSkills, userSkillSet);

        int progress = calculateProgress(matched.size(), targetSkills.size());

        Map<String, List<String>> resources = getResourcesForMissingSkills(missing);

        Map<String, List<RepoDTO>> repos = includeRepos
                ? gitHubService.getReposForSkills(missing, experienceLevel).getRepos()
                : Collections.emptyMap();

        logger.info("Analysis done: matched={}, missing={}, progress={}%",
                matched.size(), missing.size(), progress);

        return new SkillAnalysisResponseDTO(
                matched,
                missing,
                progress,
                resources,
                repos
        );
    }

    private List<String> getMatchedSkills(List<String> targetSkills, Set<String> userSkills) {
        return targetSkills.stream()
                .filter(userSkills::contains)
                .toList();
    }

    private List<String> getMissingSkills(List<String> targetSkills, Set<String> userSkills) {
        return targetSkills.stream()
                .filter(skill -> !userSkills.contains(skill))
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
