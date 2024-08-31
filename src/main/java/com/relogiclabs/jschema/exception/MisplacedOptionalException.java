package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class MisplacedOptionalException extends BaseRuntimeException {
    public MisplacedOptionalException(ErrorDetail detail) {
        super(detail);
    }
}