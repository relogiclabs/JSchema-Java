package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidPragmaValueException extends BaseRuntimeException {
    public InvalidPragmaValueException(ErrorDetail detail) {
        super(detail);
    }
}