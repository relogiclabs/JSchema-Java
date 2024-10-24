package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class InvalidFunctionException extends MultilevelRuntimeException {
    public InvalidFunctionException(String code, String message) {
        super(code, message);
    }

    public InvalidFunctionException(ErrorDetail detail) {
        super(detail);
    }

    public InvalidFunctionException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel()) return this;
        return new InvalidFunctionException(formatForSchema(getCode(),
            getMessage(), token), this);
    }
}