package com.relogiclabs.jschema.exception;

public class JsonParserException extends BaseRuntimeException {
    public JsonParserException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}