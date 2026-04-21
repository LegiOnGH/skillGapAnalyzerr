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

import java.util.List;

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

        logger.info("Adding resource for skill: {}", skill);

        if (repository.findBySkillIgnoreCase(skill).isPresent()) {
            throw new BadRequestException("Resource already exists for skill: " + skill);
        }

        List<String> cleanedResources = StringNormalizer.normalizeListPreserveCase(dto.getResources());

        if (cleanedResources.isEmpty()) {
            throw new BadRequestException("Resources cannot be empty");
        }

        SkillResource resource = SkillResource.builder()
                .skill(skill)
                .resources(cleanedResources)
                .build();

        SkillResource saved = repository.save(resource);

        logger.info("Resource created for skill: {}", skill);

        return skillResourceMapper.toDTO(saved);
    }

    public SkillResourceResponseDTO updateResource(String id, SkillResourceUpdateDTO dto) {

        logger.info("Updating resource: {}", id);

        SkillResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        if (dto.getResources() != null && !dto.getResources().isEmpty()) {

            List<String> cleanedResources = StringNormalizer.normalizeListPreserveCase(dto.getResources());

            if (cleanedResources.isEmpty()) {
                throw new BadRequestException("Resources cannot be empty");
            }

            existing.setResources(cleanedResources);
        }

        SkillResource updated = repository.save(existing);

        logger.info("Resource updated successfully: {}", id);

        return skillResourceMapper.toDTO(updated);
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
