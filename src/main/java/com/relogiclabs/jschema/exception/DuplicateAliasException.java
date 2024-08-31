package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicateAliasException extends BaseRuntimeException {
    public DuplicateAliasException(ErrorDetail detail) {
        super(detail);
    }
}