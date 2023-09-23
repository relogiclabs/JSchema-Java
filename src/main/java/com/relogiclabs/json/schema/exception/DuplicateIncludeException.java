package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class DuplicateIncludeException extends CommonException {
    public DuplicateIncludeException(ErrorDetail detail) {
        super(detail);
    }
}
