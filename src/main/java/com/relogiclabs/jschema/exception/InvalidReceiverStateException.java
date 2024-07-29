package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidReceiverStateException extends BaseRuntimeException {
    public InvalidReceiverStateException(ErrorDetail detail) {
        super(detail);
    }
}