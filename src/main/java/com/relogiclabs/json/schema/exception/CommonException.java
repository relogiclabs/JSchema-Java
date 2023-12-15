package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CommonException extends RuntimeException {
    @Getter private final String code;
    private Map<String, String> attributes;

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(String code, String message) {
        this(code, message, null);
    }

    public CommonException(ErrorDetail detail, Throwable cause) {
        this(detail.getCode(), detail.getMessage(), cause);
    }

    public CommonException(ErrorDetail detail) {
        this(detail, null);
    }

    public String getAttribute(String name) {
        if(attributes == null) return null;
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        if(attributes == null) attributes = new HashMap<>(5);
        attributes.put(name, value);
    }
}