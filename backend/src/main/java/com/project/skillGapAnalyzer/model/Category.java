package com.project.skillGapAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    @Indexed(name = "unique_category_name", unique = true)
    private String name;
}
