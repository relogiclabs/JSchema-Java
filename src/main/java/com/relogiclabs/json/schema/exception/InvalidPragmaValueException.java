package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class InvalidPragmaValueException extends CommonException {
    public InvalidPragmaValueException(ErrorDetail detail) {
        super(detail);
    }
}
