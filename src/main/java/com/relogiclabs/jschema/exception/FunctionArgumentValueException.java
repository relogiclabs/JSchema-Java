package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class FunctionArgumentValueException extends ArgumentValueException {
    FunctionArgumentValueException(ArgumentValueException cause) {
        super(cause.getCode(), cause.getMessage(), cause);
        setParameter(cause.getParameter());
    }

    public FunctionArgumentValueException(ErrorDetail detail) {
        super(detail, null);
    }

    public FunctionArgumentValueException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public MultilevelRuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        return new FunctionArgumentValueException(formatForSchema(getCode(),
            formatFunctionMessage(), token), this);
    }
}