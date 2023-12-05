package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class NoValueReceivedException extends CommonException {
    public NoValueReceivedException(ErrorDetail detail) {
        super(detail);
    }
}