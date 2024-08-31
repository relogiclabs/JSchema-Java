package com.relogiclabs.jschema.exception;

public class TargetInvocationException extends BaseRuntimeException {
    public TargetInvocationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}