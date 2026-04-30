package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.response.RoleResponseDTO;
import com.project.skillGapAnalyzer.dto.response.SkillAnalysisResponseDTO;
import com.project.skillGapAnalyzer.dto.response.RepoDTO;
import com.project.skillGapAnalyzer.enums.Experience;
import com.project.skillGapAnalyzer.model.Role;
import com.project.skillGapAnalyzer.repository.RoleRepository;
import com.project.skillGapAnalyzer.repository.SkillResourceRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final SkillResourceRepository skillResourceRepository;
    private final GitHubService gitHubService;

    public SkillService(RoleService roleService, RoleRepository roleRepository,
                        SkillResourceRepository skillResourceRepository,
                        GitHubService gitHubService) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.skillResourceRepository = skillResourceRepository;
        this.gitHubService = gitHubService;
    }

    public SkillAnalysisResponseDTO analyzeSkills(
            List<String> userSkills,
            String targetRole,
            Experience experienceLevel,
            boolean includeRepos) {

        String normalizedRole = StringNormalizer.normalizePreserveCase(targetRole);

        logger.info("Starting skill analysis | role: {}, includeRepos: {}", normalizedRole, includeRepos);

        RoleResponseDTO role = roleService.getRoleByName(normalizedRole);

        List<String> targetSkills = role.getSkills() == null
                ? List.of()
                : role.getSkills();

        Set<String> userSkillSetNormalized = userSkills == null
                ? Collections.emptySet()
                : StringNormalizer.normalizeSet(userSkills);

        logger.debug("User skills count: {}, Target skills count: {}",
                userSkillSetNormalized.size(), targetSkills.size());

        List<String> matched = getMatchedSkills(targetSkills, userSkillSetNormalized);
        List<String> missing = getMissingSkills(targetSkills, userSkillSetNormalized);

        int progress = calculateProgress(matched.size(), targetSkills.size());

        Map<String, List<String>> resources = getResourcesForMissingSkills(missing);

        Map<String, List<RepoDTO>> repos = includeRepos
                ? gitHubService.getReposForSkills(missing, experienceLevel).getReposBySkill()
                : Collections.emptyMap();

        logger.info("Analysis complete | matched: {}, missing: {}, progress: {}%",
                matched.size(), missing.size(), progress);

        return new SkillAnalysisResponseDTO(
                matched,
                missing,
                progress,
                resources,
                repos
        );
    }

    @Cacheable("allSkills")
    public Map<String, List<String>> getAllSkillsByCategory() {
        List<Role> allRoles = roleRepository.findAll();
        Map<String, Set<String>> grouped = new LinkedHashMap<>();

        for (Role role : allRoles) {
            String category = StringNormalizer.normalizePreserveCase(role.getCategory());
            grouped.computeIfAbsent(category, k -> new LinkedHashSet<>())
                    .addAll(role.getSkills() != null ? role.getSkills() : List.of());
        }

        // Convert Set → List for JSON serialization
        Map<String, List<String>> result = new LinkedHashMap<>();
        grouped.forEach((cat, skills) -> result.put(cat, new ArrayList<>(skills)));
        return result;
    }

    private List<String> getMatchedSkills(List<String> targetSkills, Set<String> normalizedUserSkills) {

        List<String> matched = targetSkills.stream()
                .filter(s -> normalizedUserSkills.contains(StringNormalizer.normalize(s)))
                .toList();

        logger.debug("Matched skills count: {}", matched.size());

        return matched;
    }

    private List<String> getMissingSkills(List<String> targetSkills, Set<String> normalizedUserSkills) {

        List<String> missing = targetSkills.stream()
                .filter(s -> !normalizedUserSkills.contains(StringNormalizer.normalize(s)))
                .toList();

        logger.debug("Missing skills count: {}", missing.size());

        return missing;
    }

    private int calculateProgress(int matched, int total) {

        if (total == 0) {
            logger.warn("No target skills found. Progress set to 0%");
            return 0;
        }
        int progress = Math.round(((float) matched / total) * 100);
        logger.debug("Calculated progress: {}%", progress);
        return progress;
    }

    public Map<String, List<String>> getResourcesForMissingSkills(List<String> missing) {
        logger.debug("Fetching resources for {} missing skills", missing.size());
        Map<String, List<String>> resources = new HashMap<>();
        for (String skill : missing) {
            skillResourceRepository.findBySkillIgnoreCase(StringNormalizer.normalize(skill))
                    .ifPresent(resource -> {
                        resources.put(skill, resource.getResources());
                        logger.debug("Resources found for skill: {}", skill);
                    });
        }
        logger.debug("Total skills with resources found: {}", resources.size());
        return resources;
    }
}
