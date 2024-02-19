package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptOperationException extends ScriptRuntimeException {
    public ScriptOperationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}