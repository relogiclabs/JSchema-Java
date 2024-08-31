package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class AliasNotFoundException extends BaseRuntimeException {
    public AliasNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}