package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.CategoryRequestDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.model.Category;
import com.project.skillGapAnalyzer.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryRequestDTO dto){

        String name = dto.getName().trim().toLowerCase();

        logger.info("Creating category: {}", name);

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Category already exists: " + name);
        }

        Category category = Category.builder()
                .name(name)
                .build();

        Category saved = categoryRepository.save(category);

        logger.info("Category created successfully: {}", saved.getId());

        return saved;
    }

    public Category updateCategory(String id, CategoryRequestDTO dto){

        logger.info("Updating category: {}", id);

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {

            String newName = dto.getName().trim().toLowerCase();

            if (categoryRepository.existsByNameIgnoreCase(newName)
                    && !existing.getName().equalsIgnoreCase(newName)) {
                throw new BadRequestException("Category already exists: " + newName);
            }

            existing.setName(newName);
        }

        Category updated = categoryRepository.save(existing);

        logger.info("Category updated successfully: {}", id);

        return updated;
    }

    public void deleteCategory(String id){

        logger.info("Deleting category: {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);

        logger.info("Category deleted successfully: {}", id);
    }

    public List<Category> getAllCategories(){

        logger.info("Fetching all categories");

        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            logger.warn("No categories found in database");
        } else {
            logger.info("Fetched {} categories", categories.size());
        }

        return categories;
    }
}
