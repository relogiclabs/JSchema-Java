package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicatePropertyKeyException extends BaseRuntimeException {
    public DuplicatePropertyKeyException(ErrorDetail detail) {
        super(detail);
    }
}