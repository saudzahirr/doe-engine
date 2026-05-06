package com.doe.engine.dto;

import java.util.Map;

public record DoeExecuteRequest(String method, Map<String, Object> params) {
}
