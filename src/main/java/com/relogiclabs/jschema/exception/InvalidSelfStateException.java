package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class InvalidSelfStateException extends InvocationRuntimeException {
    private EValue self;

    public InvalidSelfStateException(String code, String message) {
        super(code, message);
    }

    public InvalidSelfStateException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        var message = addSelf(getMessage() + " of method '" + getSubject() + "'", self);
        return new InvalidSelfStateException(formatForSchema(getCode(),
            addNative(message), token), this);
    }
}