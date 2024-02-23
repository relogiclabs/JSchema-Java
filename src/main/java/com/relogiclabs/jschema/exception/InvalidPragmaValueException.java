package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidPragmaValueException extends CommonException {
    public InvalidPragmaValueException(ErrorDetail detail) {
        super(detail);
    }
}