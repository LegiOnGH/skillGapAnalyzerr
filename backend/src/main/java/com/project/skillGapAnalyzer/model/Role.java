package com.project.skillGapAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String id;

    @Indexed(name = "unique_role_name",unique = true)
    private String roleName;

    @Indexed
    private String category;

    private List<String> skills;
}
