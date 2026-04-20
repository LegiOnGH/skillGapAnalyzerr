package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.SkillResourceRequestDTO;
import com.project.skillGapAnalyzer.dto.request.SkillResourceUpdateDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.model.SkillResource;
import com.project.skillGapAnalyzer.repository.SkillResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SkillResourceService.class);

    private final SkillResourceRepository repository;

    public SkillResourceService(SkillResourceRepository repository) {
        this.repository = repository;
    }

    public SkillResource addResource(SkillResourceRequestDTO dto) {

        String skill = dto.getSkill().trim().toLowerCase();

        logger.info("Adding resource for skill: {}", skill);

        if (repository.findBySkillIgnoreCase(skill).isPresent()) {
            throw new BadRequestException("Resource already exists for skill: " + skill);
        }

        List<String> cleanedResources = dto.getResources()
                .stream()
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (cleanedResources.isEmpty()) {
            throw new BadRequestException("Resources cannot be empty");
        }

        SkillResource resource = SkillResource.builder()
                .skill(skill)
                .resources(cleanedResources)
                .build();

        SkillResource saved = repository.save(resource);

        logger.info("Resource created for skill: {}", skill);

        return saved;
    }

    public SkillResource updateResource(String id, SkillResourceUpdateDTO dto) {

        logger.info("Updating resource: {}", id);

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        if (dto.getResources() != null && !dto.getResources().isEmpty()) {

            List<String> cleanedResources = dto.getResources()
                    .stream()
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            if (cleanedResources.isEmpty()) {
                throw new BadRequestException("Resources cannot be empty");
            }

            existing.setResources(cleanedResources);
        }

        SkillResource updated = repository.save(existing);

        logger.info("Resource updated successfully: {}", id);

        return updated;
    }

    public void deleteResource(String id) {

        logger.info("Deleting resource: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Resource deleted successfully: {}", id);
    }
}
