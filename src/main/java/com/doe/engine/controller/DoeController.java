package com.doe.engine.controller;

import com.doe.engine.dto.DoeExecuteRequest;
import com.doe.engine.dto.DoeMethodDefinition;
import com.doe.engine.service.DoeExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doe")
public class DoeController {

    private static final Logger log = LoggerFactory.getLogger(DoeController.class);

    private final DoeExecutionService doeExecutionService;

    public DoeController(DoeExecutionService doeExecutionService) {
        this.doeExecutionService = doeExecutionService;
    }

    @GetMapping("/methods")
    public List<DoeMethodDefinition> methods() {
        List<DoeMethodDefinition> methods = doeExecutionService.methods();
        log.info("DOE methods requested: count={}", methods.size());
        return methods;
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(@RequestBody DoeExecuteRequest request) {
        long startedAt = System.currentTimeMillis();
        String method = request != null ? request.method() : null;

        try {
            double[][] designMatrix = doeExecutionService.execute(request);
            long durationMs = System.currentTimeMillis() - startedAt;
            int rows = designMatrix.length;
            int cols = rows > 0 ? designMatrix[0].length : 0;
            log.info("DOE execute success: method={}, rows={}, cols={}, durationMs={}", method, rows, cols, durationMs);
            return ResponseEntity.ok(Map.of("designMatrix", designMatrix));
        } catch (IllegalArgumentException ex) {
            long durationMs = System.currentTimeMillis() - startedAt;
            log.warn("DOE execute validation failed: method={}, error={}, durationMs={}", method, ex.getMessage(), durationMs);
            return ResponseEntity.badRequest().body(errorBody(ex.getMessage()));
        } catch (Exception ex) {
            long durationMs = System.currentTimeMillis() - startedAt;
            log.error("DOE execute failed: method={}, durationMs={}", method, durationMs, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody("Internal error while executing DOE method"));
        }
    }

    private Map<String, Object> errorBody(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("designMatrix", null);
        body.put("error", message);
        return body;
    }
}
