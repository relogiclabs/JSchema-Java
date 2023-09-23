package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class InvalidFunctionException extends CommonException {
    public InvalidFunctionException(String code, String message) {
        super(code, message);
    }

    public InvalidFunctionException(ErrorDetail detail) {
        super(detail);
    }
}
