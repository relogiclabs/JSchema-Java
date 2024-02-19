package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class MisplacedOptionalException extends CommonException {
    public MisplacedOptionalException(ErrorDetail detail) {
        super(detail);
    }
}