package com.project.skillGapAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "analysis")
@NoArgsConstructor
@AllArgsConstructor
public class Analysis {

    @Id
    private String id;
    private String userId;
    private String roleName;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private int progress;
    private LocalDateTime createdAt;
}
