package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptThrowInitiatedException extends BaseRuntimeException {
    public ScriptThrowInitiatedException(ErrorDetail detail) {
        super(detail);
    }
}