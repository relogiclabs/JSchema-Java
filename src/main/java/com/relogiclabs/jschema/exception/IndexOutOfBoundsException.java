package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class IndexOutOfBoundsException extends BaseRuntimeException {
    public IndexOutOfBoundsException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}