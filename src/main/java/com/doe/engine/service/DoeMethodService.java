package com.doe.engine.service;

import com.doe.engine.dto.DoeMethodDefinition;

import java.util.Map;

public interface DoeMethodService {

    String methodName();

    DoeMethodDefinition definition();

    double[][] generate(Map<String, Object> params);
}
