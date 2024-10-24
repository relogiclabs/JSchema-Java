package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class ClassInstantiationException extends MultilevelRuntimeException {
    public ClassInstantiationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ClassInstantiationException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel()) return this;
        return new ClassInstantiationException(formatForSchema(getCode(),
            getMessage(), token), this);
    }
}