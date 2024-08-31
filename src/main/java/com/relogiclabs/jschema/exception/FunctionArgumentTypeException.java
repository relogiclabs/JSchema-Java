package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class FunctionArgumentTypeException extends ArgumentTypeException {
    FunctionArgumentTypeException(ArgumentTypeException cause) {
        super(cause.getCode(), cause.getMessage(), cause);
        setParameter(cause.getParameter());
    }

    public FunctionArgumentTypeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        return new FunctionArgumentTypeException(formatForSchema(getCode(),
            formatFunctionMessage(), token), this);
    }
}