package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;

public class PragmaNotFoundException extends CommonException {
    public PragmaNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}
