package com.doe.engine.service;

import com.doe.algorithms.BoxBehnkenDOE;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeParamDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoxBehnkenService implements DoeMethodService {

    @Override
    public String methodName() {
        return "box-behnken";
    }

    @Override
    public DoeMethodDefinition definition() {
        return new DoeMethodDefinition(
                methodName(),
                "Box-Behnken",
                "Response surface design with three levels.",
                List.of(new DoeParamDefinition("factors", "Factors", "integer", 3, null, 4))
        );
    }

    @Override
    public double[][] generate(Map<String, Object> params) {
        int factors = requireInt(params, "factors");
        if (factors < 3) {
            throw new IllegalArgumentException("params.factors must be >= 3 for box-behnken");
        }
        return BoxBehnkenDOE.boxBehnkenDesign(factors).getData();
    }

    private int requireInt(Map<String, Object> params, String key) {
        if (params == null) {
            throw new IllegalArgumentException("params are required for box-behnken");
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
