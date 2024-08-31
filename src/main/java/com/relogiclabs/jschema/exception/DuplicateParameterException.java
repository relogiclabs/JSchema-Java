package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicateParameterException extends BaseRuntimeException {
    public DuplicateParameterException(ErrorDetail detail) {
        super(detail);
    }
}