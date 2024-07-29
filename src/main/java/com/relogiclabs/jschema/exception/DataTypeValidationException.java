package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DataTypeValidationException extends ValidationException {
    private String typeBaseName;

    public DataTypeValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual) {
        super(error, expected, actual);
    }

    public DataTypeValidationException(ErrorDetail error, ExpectedDetail expected,
                ActualDetail actual, Throwable cause) {
        super(error, expected, actual, cause);
    }
}