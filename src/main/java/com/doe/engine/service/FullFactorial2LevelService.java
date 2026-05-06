package com.doe.engine.service;

import com.doe.algorithms.FactorialDOE;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeParamDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FullFactorial2LevelService implements DoeMethodService {

    @Override
    public String methodName() {
        return "full-factorial-2level";
    }

    @Override
    public DoeMethodDefinition definition() {
        return new DoeMethodDefinition(
                methodName(),
                "Full Factorial (2-level)",
                "Complete 2-level factorial combinations.",
                List.of(new DoeParamDefinition("factors", "Factors", "integer", 1, null, 3))
        );
    }

    @Override
    public double[][] generate(Map<String, Object> params) {
        int factors = requireInt(params, "factors");
        if (factors < 1) {
            throw new IllegalArgumentException("params.factors must be >= 1 for full-factorial-2level");
        }
        return FactorialDOE.fullFactorial2Level(factors).getData();
    }

    private int requireInt(Map<String, Object> params, String key) {
        if (params == null) {
            throw new IllegalArgumentException("params are required for full-factorial-2level");
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
