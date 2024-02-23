package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicateDefinitionException extends CommonException {
    public DuplicateDefinitionException(ErrorDetail detail) {
        super(detail);
    }
}