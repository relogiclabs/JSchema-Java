package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicatePragmaException extends BaseRuntimeException {
    public DuplicatePragmaException(ErrorDetail detail) {
        super(detail);
    }
}