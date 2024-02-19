package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidDataTypeException extends CommonException {
    public InvalidDataTypeException(ErrorDetail detail) {
        super(detail);
    }
}