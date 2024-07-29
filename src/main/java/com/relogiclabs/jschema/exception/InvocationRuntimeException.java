package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class InvocationRuntimeException extends MultilevelRuntimeException {
    private String subject;

    public InvocationRuntimeException(String message) {
        super(message);
    }

    public InvocationRuntimeException(String code, String message) {
        super(code, message);
    }

    public InvocationRuntimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InvocationRuntimeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || subject == null) return this;
        return new InvocationRuntimeException(formatForSchema(getCode(),
            getMessage() + " '" + subject + "'", token), this);
    }
}