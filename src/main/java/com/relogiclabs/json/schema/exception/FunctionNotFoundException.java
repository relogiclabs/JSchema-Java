package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class FunctionNotFoundException extends CommonException {
    public FunctionNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}
