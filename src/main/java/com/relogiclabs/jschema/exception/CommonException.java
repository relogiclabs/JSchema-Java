package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.copyOfRange;

public class CommonException extends RuntimeException {
    private static final String FAIL_METHOD_PREFIX = "fail";
    @Getter private final String code;
    private Map<String, String> attributes;

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        formatStackTrace();
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

    @Override
    public synchronized Throwable fillInStackTrace() {
        var result = super.fillInStackTrace();
        formatStackTrace();
        return result;
    }

    private void formatStackTrace() {
        StackTraceElement[] stackTrace = getStackTrace();
        int offset = 0;
        for(var e : stackTrace)
            if(e.getMethodName().startsWith(FAIL_METHOD_PREFIX)) offset++;
            else break;
        setStackTrace(copyOfRange(stackTrace, offset, stackTrace.length));
    }
}