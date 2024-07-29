package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class ScriptIteratorException extends MultilevelRuntimeException {
    public ScriptIteratorException(String code, String message) {
        super(code, message);
    }

    public ScriptIteratorException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel()) return this;
        return new ScriptIteratorException(formatForSchema(getCode(),
            getMessage(), token), this);
    }
}