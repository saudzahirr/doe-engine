package com.doe.engine.service;

import com.doe.algorithms.DoehlertDOE;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeParamDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoehlertShellService implements DoeMethodService {

    @Override
    public String methodName() {
        return "doehlert-shell";
    }

    @Override
    public DoeMethodDefinition definition() {
        return new DoeMethodDefinition(
                methodName(),
                "Doehlert Shell",
                "Doehlert shell design with configurable shells.",
                List.of(
                        new DoeParamDefinition("factors", "Factors", "integer", 2, null, 3),
                        new DoeParamDefinition("shells", "Shells", "integer", 1, null, 1)
                )
        );
    }

    @Override
    public double[][] generate(Map<String, Object> params) {
        int factors = requireInt(params, "factors");
        int shells = requireInt(params, "shells");

        if (factors < 2) {
            throw new IllegalArgumentException("params.factors must be >= 2 for doehlert-shell");
        }
        if (shells < 1) {
            throw new IllegalArgumentException("params.shells must be >= 1 for doehlert-shell");
        }

        return DoehlertDOE.doehlertShellDesign(factors, shells).getData();
    }

    private int requireInt(Map<String, Object> params, String key) {
        if (params == null) {
            throw new IllegalArgumentException("params are required for doehlert-shell");
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
