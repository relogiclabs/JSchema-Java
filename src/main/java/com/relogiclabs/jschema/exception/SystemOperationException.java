package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class SystemOperationException extends ScriptRuntimeException {
    public SystemOperationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}