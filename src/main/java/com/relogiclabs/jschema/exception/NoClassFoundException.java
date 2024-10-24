package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class NoClassFoundException extends BaseRuntimeException {
    public NoClassFoundException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}