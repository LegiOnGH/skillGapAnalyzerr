package com.project.skillGapAnalyzer.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole{
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SUPER_ADMIN;

    @JsonValue
    public String getValue() {
        return name().replace("ROLE_", "");
    }
}
