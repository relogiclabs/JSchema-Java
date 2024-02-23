package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidFunctionException extends CommonException {
    public InvalidFunctionException(String code, String message) {
        super(code, message);
    }

    public InvalidFunctionException(ErrorDetail detail) {
        super(detail);
    }
}