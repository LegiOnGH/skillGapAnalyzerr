package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.response.CategoryResponseDTO;
import com.project.skillGapAnalyzer.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDTO toDTO(Category category){
        if (category == null) return null;

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName().trim())
                .build();
    }
}
