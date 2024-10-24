package com.relogiclabs.jschema.exception;

public class TypeNotSupportedException extends BaseRuntimeException {
    public TypeNotSupportedException(String code, String message) {
        super(code, message);
    }
}