package com.project.skillGapAnalyzer.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringNormalizer {

    private StringNormalizer() {}

    public static String normalize(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }

    public static String normalizePreserveCase(String input) {
        return input == null ? null : input.trim();
    }

    public static List<String> normalizeList(List<String> inputs) {
        if (inputs == null) return List.of();

        return inputs.stream()
                .map(StringNormalizer::normalize)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .toList();
    }

    public static List<String> normalizeListPreserveCase(List<String> inputs) {
        if (inputs == null) return List.of();

        Set<String> seen = new HashSet<>();

        return inputs.stream()
                .map(StringNormalizer::normalizePreserveCase)
                .filter(s -> s != null && !s.isEmpty())
                .filter(s -> seen.add(s.toLowerCase()))
                .toList();
    }

    public static Set<String> normalizeSet(List<String> inputs) {
        return Set.copyOf(normalizeList(inputs));
    }
}
