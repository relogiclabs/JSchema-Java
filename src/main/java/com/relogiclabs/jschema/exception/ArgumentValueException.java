package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ArgumentValueException extends InvalidArgumentException {
    public ArgumentValueException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}