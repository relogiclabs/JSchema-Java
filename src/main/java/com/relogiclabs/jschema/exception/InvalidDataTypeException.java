package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidDataTypeException extends BaseRuntimeException {
    public InvalidDataTypeException(ErrorDetail detail) {
        super(detail);
    }
}