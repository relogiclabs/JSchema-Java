package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ClassInstantiationException extends BaseRuntimeException {
    public ClassInstantiationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}