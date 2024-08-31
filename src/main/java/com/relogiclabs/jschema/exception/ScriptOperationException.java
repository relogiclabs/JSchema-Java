package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptOperationException extends BaseRuntimeException {
    public ScriptOperationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}