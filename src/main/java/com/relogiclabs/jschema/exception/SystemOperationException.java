package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class SystemOperationException extends BaseRuntimeException {
    public SystemOperationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}