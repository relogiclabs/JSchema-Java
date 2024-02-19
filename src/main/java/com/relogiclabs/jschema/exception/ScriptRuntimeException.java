package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptRuntimeException extends ScriptCommonException {

    public ScriptRuntimeException(String code, String message) {
        super(code, message);
    }

    public ScriptRuntimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ScriptRuntimeException(ErrorDetail detail) {
        super(detail);
    }

    public ScriptRuntimeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}