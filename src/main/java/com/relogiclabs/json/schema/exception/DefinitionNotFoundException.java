package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class DefinitionNotFoundException extends CommonException {
    public DefinitionNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}
