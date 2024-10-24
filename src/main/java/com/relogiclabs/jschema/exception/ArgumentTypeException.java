package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ArgumentTypeException extends InvalidArgumentException {
    public ArgumentTypeException(String code, String message) {
        super(code, message);
    }

    public ArgumentTypeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}