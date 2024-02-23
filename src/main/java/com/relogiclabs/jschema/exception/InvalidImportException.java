package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidImportException extends CommonException {
    public InvalidImportException(ErrorDetail detail) {
        super(detail);
    }
}