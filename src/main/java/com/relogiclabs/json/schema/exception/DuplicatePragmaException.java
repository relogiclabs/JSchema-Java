package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class DuplicatePragmaException extends CommonException {
    public DuplicatePragmaException(ErrorDetail detail) {
        super(detail);
    }
}
