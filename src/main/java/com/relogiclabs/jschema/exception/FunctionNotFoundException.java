package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class FunctionNotFoundException extends CommonException {
    public FunctionNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}