package com.relogiclabs.jschema.exception;

public class DuplicateMethodException extends BaseRuntimeException {
    public DuplicateMethodException(String code, String message) {
        super(code, message);
    }
}