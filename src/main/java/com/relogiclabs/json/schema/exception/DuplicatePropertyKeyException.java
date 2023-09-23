package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class DuplicatePropertyKeyException extends CommonException {
    public DuplicatePropertyKeyException(ErrorDetail detail) {
        super(detail);
    }
}
