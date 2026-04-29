package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.CategoryRequestDTO;
import com.project.skillGapAnalyzer.dto.response.CategoryResponseDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.mapper.CategoryMapper;
import com.project.skillGapAnalyzer.model.Category;
import com.project.skillGapAnalyzer.repository.CategoryRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto){

        String name = StringNormalizer.normalizePreserveCase(dto.getName());

        logger.debug("Creating category: {}", name);

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Category already exists: " + name);
        }

        Category category = Category.builder()
                .name(name)
                .build();

        Category saved = categoryRepository.save(category);

        logger.info("Category created successfully: {}", saved.getId());

        return categoryMapper.toDTO(saved);
    }

    public List<CategoryResponseDTO> getAllCategories(){

        logger.debug("Fetching all categories");

        List<Category> categories = categoryRepository.findAll(Sort.by("name"));

        if (categories.isEmpty()) {
            logger.info("No categories found");
        } else {
            logger.debug("Fetched {} categories", categories.size());
        }

        return categories.stream()
                .map(categoryMapper::toDTO)
                .toList();
    }
}
