package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidReturnTypeException extends BaseRuntimeException {
    public InvalidReturnTypeException(String code, String message) {
        super(code, message);
    }

    public InvalidReturnTypeException(ErrorDetail detail) {
        super(detail);
    }
}