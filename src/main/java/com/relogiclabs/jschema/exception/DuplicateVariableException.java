package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class DuplicateVariableException extends MultilevelRuntimeException {
    public DuplicateVariableException(String code, String message) {
        super(code, message);
    }

    public DuplicateVariableException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel()) return this;
        return new DuplicateVariableException(formatForSchema(getCode(),
            getMessage(), token), this);
    }
}