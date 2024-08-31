package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public class MethodArgumentValueException extends ArgumentValueException {
    private EValue self;

    MethodArgumentValueException(EValue self, ArgumentValueException cause) {
        super(cause.getCode(), cause.getMessage(), cause);
        setParameter(cause.getParameter());
        this.self = self;
    }

    public MethodArgumentValueException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        return new MethodArgumentValueException(formatForSchema(getCode(),
            formatMethodMessage(self), token), this);
    }
}