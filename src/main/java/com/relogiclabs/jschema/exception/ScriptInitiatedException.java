package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptInitiatedException extends ScriptRuntimeException {
    public ScriptInitiatedException(String code, String message) {
        super(code, message);
    }

    public ScriptInitiatedException(ErrorDetail detail) {
        super(detail);
    }
}