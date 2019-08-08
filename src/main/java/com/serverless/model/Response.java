package com.serverless.model;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Response {
    private String message;
    private String tableName;
    private String description;
    private Map<String, Object> result;
}
