package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ScriptCommonException extends CommonException {
    public ScriptCommonException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ScriptCommonException(String code, String message) {
        super(code, message);
    }

    public ScriptCommonException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    public ScriptCommonException(ErrorDetail detail) {
        super(detail);
    }
}