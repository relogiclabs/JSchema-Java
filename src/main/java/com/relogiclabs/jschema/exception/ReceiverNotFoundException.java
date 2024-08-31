package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class ReceiverNotFoundException extends BaseRuntimeException {
    public ReceiverNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}