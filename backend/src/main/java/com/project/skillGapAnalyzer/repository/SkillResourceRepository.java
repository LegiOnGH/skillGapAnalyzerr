package com.project.skillGapAnalyzer.repository;

import com.project.skillGapAnalyzer.model.SkillResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SkillResourceRepository extends MongoRepository<SkillResource, String> {
    Optional<SkillResource> findBySkillIgnoreCase(String skill);
}
