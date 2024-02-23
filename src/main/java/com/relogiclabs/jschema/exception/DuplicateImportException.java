package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicateImportException extends CommonException {
    public DuplicateImportException(ErrorDetail detail) {
        super(detail);
    }
}