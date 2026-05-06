package com.doe.engine.dto;

import java.util.List;

public record DoeMethodDefinition(
        String method,
        String label,
        String description,
        List<DoeParamDefinition> params
) {
}
