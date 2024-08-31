package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class PragmaNotFoundException extends BaseRuntimeException {
    public PragmaNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}