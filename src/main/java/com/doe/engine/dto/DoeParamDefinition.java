package com.doe.engine.dto;

public record DoeParamDefinition(
        String key,
        String label,
        String type,
        Integer min,
        Integer max,
        Object defaultValue
) {
}
