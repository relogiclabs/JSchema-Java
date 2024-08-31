package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidImportException extends BaseRuntimeException {
    public InvalidImportException(ErrorDetail detail) {
        super(detail);
    }
}