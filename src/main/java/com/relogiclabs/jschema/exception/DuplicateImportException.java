package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicateImportException extends BaseRuntimeException {
    public DuplicateImportException(ErrorDetail detail) {
        super(detail);
    }
}