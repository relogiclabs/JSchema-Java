package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidLeftValueException extends BaseRuntimeException {
    public InvalidLeftValueException(ErrorDetail detail) {
        super(detail);
    }
}