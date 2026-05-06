package com.doe.engine.service;

import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.dto.DoeExecuteRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoeExecutionService {

    private final Map<String, MethodExecutor> methodExecutors;
    private final List<DoeMethodDefinition> methodDefinitions;

    public DoeExecutionService(List<DoeMethodService> services, List<MultiDoeMethodService> multiServices) {
        Map<String, MethodExecutor> tempExecutors = new HashMap<>();

        for (DoeMethodService service : services) {
            String key = service.methodName().toLowerCase();
            putOrThrow(tempExecutors, key, params -> service.generate(params), service.definition());
        }

        for (MultiDoeMethodService service : multiServices) {
            for (DoeMethodDefinition definition : service.definitions()) {
                String key = definition.method().toLowerCase();
                putOrThrow(tempExecutors, key, params -> service.generate(key, params), definition);
            }
        }

        this.methodExecutors = Map.copyOf(tempExecutors);
        this.methodDefinitions = tempExecutors.values().stream()
                .map(MethodExecutor::definition)
                .sorted(Comparator.comparing(DoeMethodDefinition::method))
                .toList();
    }

    public List<DoeMethodDefinition> methods() {
        return methodDefinitions;
    }

    public double[][] execute(DoeExecuteRequest request) {
        if (request == null || request.method() == null || request.method().isBlank()) {
            throw new IllegalArgumentException("method is required");
        }

        String method = request.method().trim().toLowerCase();

        MethodExecutor executor = methodExecutors.get(method);
        if (executor == null) {
            throw new IllegalArgumentException("Unsupported method: " + request.method());
        }

        return executor.execute(request.params());
    }

    private void putOrThrow(
            Map<String, MethodExecutor> map,
            String key,
            MatrixGenerator generator,
            DoeMethodDefinition definition
    ) {
        if (map.containsKey(key)) {
            throw new IllegalStateException("Duplicate DOE method registration: " + key);
        }
        map.put(key, new MethodExecutor(generator, definition));
    }

    private record MethodExecutor(MatrixGenerator execute, DoeMethodDefinition definition) {
    }

    @FunctionalInterface
    private interface MatrixGenerator {
        double[][] execute(Map<String, Object> params);
    }
}
