package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.SkillResourceRequestDTO;
import com.project.skillGapAnalyzer.dto.request.SkillResourceUpdateDTO;
import com.project.skillGapAnalyzer.dto.response.SkillResourceResponseDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.mapper.SkillResourceMapper;
import com.project.skillGapAnalyzer.model.SkillResource;
import com.project.skillGapAnalyzer.repository.SkillResourceRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class SkillResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SkillResourceService.class);

    private final SkillResourceRepository repository;
    private final SkillResourceMapper skillResourceMapper;

    public SkillResourceService(SkillResourceRepository repository, SkillResourceMapper skillResourceMapper) {
        this.repository = repository;
        this.skillResourceMapper = skillResourceMapper;
    }

    public SkillResourceResponseDTO addResource(SkillResourceRequestDTO dto) {

        String skill = StringNormalizer.normalize(dto.getSkill());

        List<String> newResources = StringNormalizer.normalizeListPreserveCase(dto.getResources());

        if (newResources.isEmpty()) {
            throw new BadRequestException("Resources cannot be empty");
        }

        Optional<SkillResource> optional = repository.findBySkillIgnoreCase(skill);

        if (optional.isPresent()) {

            SkillResource existing = optional.get();

            List<String> existingResources =
                    existing.getResources() != null ? existing.getResources() : new ArrayList<>();

            List<String> updatedResources = new ArrayList<>(existingResources);
            updatedResources.addAll(newResources);

            existing.setResources(
                    new LinkedHashSet<>(updatedResources).stream().toList()
            );

            SkillResource saved = repository.save(existing);

            return skillResourceMapper.toDTO(saved);

        } else {

            SkillResource resource = SkillResource.builder()
                    .skill(skill)
                    .resources(newResources)
                    .build();

            SkillResource saved = repository.save(resource);

            return skillResourceMapper.toDTO(saved);
        }
    }

    public SkillResourceResponseDTO updateResource(
            String id,
            SkillResourceUpdateDTO dto
    ) {

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        List<String> resources = existing.getResources();

        if (resources == null || resources.isEmpty()) {
            throw new BadRequestException("No resources found for this skill");
        }

        String normalizedOld = StringNormalizer.normalizePreserveCase(dto.getOldResource());
        String normalizedNew = StringNormalizer.normalizePreserveCase(dto.getNewResource());

        boolean removed = resources.removeIf(r -> r.equalsIgnoreCase(normalizedOld));

        if (!removed) {
            throw new BadRequestException("Resource not found: " + dto.getOldResource());
        }

        if (resources.stream().noneMatch(r -> r.equalsIgnoreCase(normalizedNew))) {
            resources.add(normalizedNew);
        }

        existing.setResources(resources);

        SkillResource saved = repository.save(existing);

        return skillResourceMapper.toDTO(saved);
    }

    public void deleteResource(String id, String resourceToDelete) {

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        List<String> resources = existing.getResources();

        if (resources == null || resources.isEmpty()) {
            throw new BadRequestException("No resources found for this skill");
        }

        String normalized = StringNormalizer.normalizePreserveCase(resourceToDelete);

        boolean removed = resources.removeIf(r -> r.equalsIgnoreCase(normalized));

        if (!removed) {
            throw new BadRequestException("Resource not found: " + resourceToDelete);
        }

        existing.setResources(resources);

        SkillResource saved = repository.save(existing);

        skillResourceMapper.toDTO(saved);
    }
}
