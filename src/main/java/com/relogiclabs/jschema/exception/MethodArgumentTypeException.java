package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class MethodArgumentTypeException extends ArgumentTypeException {
    private EValue self;

    MethodArgumentTypeException(EValue self, ArgumentTypeException cause) {
        super(cause.getCode(), cause.getMessage(), cause);
        setParameter(cause.getParameter());
        this.self = self;
    }

    public MethodArgumentTypeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        return new MethodArgumentTypeException(formatForSchema(getCode(),
            formatMethodMessage(self), token), this);
    }
}