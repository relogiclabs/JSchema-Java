package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicatePragmaException extends CommonException {
    public DuplicatePragmaException(ErrorDetail detail) {
        super(detail);
    }
}