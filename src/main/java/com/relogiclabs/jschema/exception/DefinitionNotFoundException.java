package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DefinitionNotFoundException extends CommonException {
    public DefinitionNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}