package com.relogiclabs.jschema.exception;

public class InvalidDateTimeException extends CommonException {
    public InvalidDateTimeException(String code, String message) {
        super(code, message);
    }

    public InvalidDateTimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}