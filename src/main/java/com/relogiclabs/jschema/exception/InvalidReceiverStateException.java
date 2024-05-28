package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class InvalidReceiverStateException extends CommonException {
    public InvalidReceiverStateException(ErrorDetail detail) {
        super(detail);
    }
}