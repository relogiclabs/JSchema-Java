package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class ClassInstantiationException extends CommonException {
    public ClassInstantiationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }
}
