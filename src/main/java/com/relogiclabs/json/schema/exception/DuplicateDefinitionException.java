package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class DuplicateDefinitionException extends CommonException {
    public DuplicateDefinitionException(ErrorDetail detail) {
        super(detail);
    }
}
