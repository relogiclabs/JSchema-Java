package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class FunctionMismatchException extends CommonException {
    public FunctionMismatchException(ErrorDetail detail) {
        super(detail);
    }
}
