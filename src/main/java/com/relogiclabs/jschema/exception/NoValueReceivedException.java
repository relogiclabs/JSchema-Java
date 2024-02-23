package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class NoValueReceivedException extends CommonException {
    public NoValueReceivedException(ErrorDetail detail) {
        super(detail);
    }
}