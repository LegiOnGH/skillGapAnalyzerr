package com.project.skillGapAnalyzer.repository;

import com.project.skillGapAnalyzer.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByNameIgnoreCase(String categoryName);
}
