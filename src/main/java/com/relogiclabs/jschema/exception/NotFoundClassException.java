package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class NotFoundClassException extends CommonException {
    public NotFoundClassException(ErrorDetail detail) {
        super(detail);
    }
}