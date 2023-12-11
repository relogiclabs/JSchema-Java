package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class MisplacedOptionalException extends CommonException {
    public MisplacedOptionalException(ErrorDetail detail) {
        super(detail);
    }
}