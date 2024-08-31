package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class UpdateNotSupportedException extends MultilevelRuntimeException {
    public UpdateNotSupportedException(String code, String message) {
        super(code, message);
    }

    public UpdateNotSupportedException(ErrorDetail detail) {
        super(detail);
    }

    public UpdateNotSupportedException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel()) return this;
        return new UpdateNotSupportedException(formatForSchema(getCode(),
            getMessage(), token), this);
    }
}