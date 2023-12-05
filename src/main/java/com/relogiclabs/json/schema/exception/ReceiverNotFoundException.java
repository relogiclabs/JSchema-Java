package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class ReceiverNotFoundException extends CommonException {
    public ReceiverNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}