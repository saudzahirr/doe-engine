package com.doe.engine.service;

import com.doe.algorithms.DoehlertDOE;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeParamDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoehlertSimplexService implements DoeMethodService {

    @Override
    public String methodName() {
        return "doehlert-simplex";
    }

    @Override
    public DoeMethodDefinition definition() {
        return new DoeMethodDefinition(
                methodName(),
                "Doehlert Simplex",
                "Uniform shell/simplex design around center.",
                List.of(new DoeParamDefinition("factors", "Factors", "integer", 2, null, 3))
        );
    }

    @Override
    public double[][] generate(Map<String, Object> params) {
        int factors = requireInt(params, "factors");
        if (factors < 2) {
            throw new IllegalArgumentException("params.factors must be >= 2 for doehlert-simplex");
        }
        return DoehlertDOE.doehlertSimplexDesign(factors).getData();
    }

    private int requireInt(Map<String, Object> params, String key) {
        if (params == null) {
            throw new IllegalArgumentException("params are required for doehlert-simplex");
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
