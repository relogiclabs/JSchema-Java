package com.relogiclabs.jschema.exception;

public class DataOwnerMismatchedException extends BaseRuntimeException {
    public DataOwnerMismatchedException(String code, String message) {
        super(code, message);
    }
}