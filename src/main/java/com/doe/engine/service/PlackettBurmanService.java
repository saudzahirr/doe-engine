package com.doe.engine.service;

import com.doe.algorithms.DoePlackettBurman;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeParamDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlackettBurmanService implements DoeMethodService {

    @Override
    public String methodName() {
        return "plackett-burman";
    }

    @Override
    public DoeMethodDefinition definition() {
        return new DoeMethodDefinition(
                methodName(),
                "Plackett-Burman",
                "Screening design for many factors in fewer runs.",
                List.of(new DoeParamDefinition("factors", "Factors", "integer", 1, null, 7))
        );
    }

    @Override
    public double[][] generate(Map<String, Object> params) {
        int factors = requireInt(params, "factors");
        if (factors < 1) {
            throw new IllegalArgumentException("params.factors must be >= 1 for plackett-burman");
        }
        return DoePlackettBurman.pbdesign(factors).getData();
    }

    private int requireInt(Map<String, Object> params, String key) {
        if (params == null) {
            throw new IllegalArgumentException("params are required for plackett-burman");
        }

        Object value = params.get(key);
        if (value == null) {
            throw new IllegalArgumentException("params." + key + " is required");
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("params." + key + " must be an integer");
        }
    }
}
