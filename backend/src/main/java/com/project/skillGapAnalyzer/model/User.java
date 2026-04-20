package com.project.skillGapAnalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.skillGapAnalyzer.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private UserRole role;

}
