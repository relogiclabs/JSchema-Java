package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;

public class ValueValidationException extends ValidationException {
    public ValueValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual) {
        super(error, expected, actual);
    }

    public ValueValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual, Throwable cause) {
        super(error, expected, actual, cause);
    }
}