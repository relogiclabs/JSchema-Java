package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class FunctionNotFoundException extends BaseRuntimeException {
    public FunctionNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}