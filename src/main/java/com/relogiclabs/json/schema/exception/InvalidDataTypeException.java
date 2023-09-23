package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class InvalidDataTypeException extends CommonException {
    public InvalidDataTypeException(ErrorDetail detail) {
        super(detail);
    }
}
