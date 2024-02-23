package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class DuplicatePropertyKeyException extends CommonException {
    public DuplicatePropertyKeyException(ErrorDetail detail) {
        super(detail);
    }
}