package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class VariableNotFoundException extends BaseRuntimeException {
    public VariableNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}