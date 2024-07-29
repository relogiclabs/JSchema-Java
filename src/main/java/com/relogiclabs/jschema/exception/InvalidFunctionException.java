package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidFunctionException extends BaseRuntimeException {
    public InvalidFunctionException(ErrorDetail detail) {
        super(detail);
    }
}