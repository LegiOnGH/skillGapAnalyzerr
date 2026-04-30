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
import org.springframework.data.domain.Sort;
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

        logger.debug("Adding resource(s) for skill: {}", skill);

        List<String> newResources = StringNormalizer.normalizeListPreserveCase(dto.getResources());

        if (newResources.isEmpty()) {
            throw new BadRequestException("Resources cannot be empty");
        }

        Optional<SkillResource> optional = repository.findBySkillIgnoreCase(skill);

        if (optional.isPresent()) {

            SkillResource existing = optional.get();

            List<String> existingResources = existing.getResources() != null
                    ? new ArrayList<>(existing.getResources())
                    : new ArrayList<>();

            existingResources.addAll(newResources);

            List<String> deduplicated = new LinkedHashSet<>(existingResources)
                    .stream()
                    .toList();

            existing.setResources(deduplicated);

            SkillResource saved = repository.save(existing);

            logger.info("Resources updated successfully for skill: {}", skill);

            return skillResourceMapper.toDTO(saved);

        } else {

            SkillResource resource = SkillResource.builder()
                    .skill(skill)
                    .resources(newResources)
                    .build();

            SkillResource saved = repository.save(resource);

            logger.info("New skill resource created for skill: {}", skill);

            return skillResourceMapper.toDTO(saved);
        }
    }

    public List<SkillResourceResponseDTO> getAllResources() {
        return repository.findAll(Sort.by("skill"))
                .stream()
                .map(skillResourceMapper::toDTO)
                .toList();
    }

    public SkillResourceResponseDTO updateResource(String id, SkillResourceUpdateDTO dto) {

        logger.debug("Updating resource for skillId: {}", id);

        if (dto.getOldResource() == null || dto.getNewResource() == null) {
            throw new BadRequestException("Invalid resource update request");
        }

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        if (existing.getResources() == null || existing.getResources().isEmpty()) {
            throw new BadRequestException("No resources found for this skill");
        }

        List<String> resources = new ArrayList<>(existing.getResources());

        String normalizedOld = StringNormalizer.normalizePreserveCase(dto.getOldResource());
        String normalizedNew = StringNormalizer.normalizePreserveCase(dto.getNewResource());

        boolean removed = resources.removeIf(r -> r.equalsIgnoreCase(normalizedOld));

        if (!removed) {
            logger.warn("Old resource not found: {}", dto.getOldResource());
            throw new BadRequestException("Resource not found: " + dto.getOldResource());
        }

        if (resources.stream().noneMatch(r -> r.equalsIgnoreCase(normalizedNew))) {
            resources.add(normalizedNew);
        }

        existing.setResources(resources);

        SkillResource saved = repository.save(existing);

        logger.info("Resource updated successfully for skillId: {}", id);

        return skillResourceMapper.toDTO(saved);
    }

    public void deleteResource(String id, String resourceToDelete) {

        logger.debug("Deleting resource for skillId: {} | resource: {}", id, resourceToDelete);

        if (resourceToDelete == null) {
            throw new BadRequestException("Resource to delete cannot be null");
        }

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        if (existing.getResources() == null || existing.getResources().isEmpty()) {
            throw new BadRequestException("No resources found for this skill");
        }

        List<String> resources = new ArrayList<>(existing.getResources());

        String normalized = StringNormalizer.normalizePreserveCase(resourceToDelete);

        boolean removed = resources.removeIf(r -> r.equalsIgnoreCase(normalized));

        if (!removed) {
            logger.warn("Resource not found for deletion: {}", resourceToDelete);
            throw new BadRequestException("Resource not found: " + resourceToDelete);
        }

        existing.setResources(resources);

        repository.save(existing);

        logger.info("Resource deleted successfully for skillId: {}", id);
    }
}
