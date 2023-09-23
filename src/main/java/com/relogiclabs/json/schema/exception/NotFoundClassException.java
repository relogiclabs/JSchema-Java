package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class NotFoundClassException extends CommonException {
    public NotFoundClassException(ErrorDetail detail) {
        super(detail);
    }
}
