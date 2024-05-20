package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class NoClassFoundException extends CommonException {
    public NoClassFoundException(ErrorDetail detail) {
        super(detail);
    }
}