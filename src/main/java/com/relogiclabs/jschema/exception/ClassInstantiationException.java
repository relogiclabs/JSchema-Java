package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ClassInstantiationException extends CommonException {
    public ClassInstantiationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}