package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class InvalidIncludeException extends CommonException {
    public InvalidIncludeException(ErrorDetail detail) {
        super(detail);
    }
}
