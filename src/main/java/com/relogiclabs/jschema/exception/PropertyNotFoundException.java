package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;

public class PropertyNotFoundException extends BaseRuntimeException {
    public PropertyNotFoundException(ErrorDetail detail) {
        super(detail);
    }
}