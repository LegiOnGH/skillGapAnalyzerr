package com.project.skillGapAnalyzer.repository;

import com.project.skillGapAnalyzer.model.Analysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisRepository extends MongoRepository<Analysis, String> {
    List<Analysis> findByUserIdOrderByCreatedAtDesc(String userId);
}
