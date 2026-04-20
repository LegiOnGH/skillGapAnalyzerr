package com.project.skillGapAnalyzer.repository;

import com.project.skillGapAnalyzer.model.Analysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends MongoRepository<Analysis, String> {
}
