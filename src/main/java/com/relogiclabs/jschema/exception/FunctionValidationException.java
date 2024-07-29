package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;

public class FunctionValidationException extends ValidationException {
    public FunctionValidationException(ErrorDetail error, JNode caller, JNode target) {
        super(error, caller, target);
    }

    public FunctionValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual) {
        super(error, expected, actual);
    }

    public FunctionValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual, Throwable cause) {
        super(error, expected, actual, cause);
    }
}