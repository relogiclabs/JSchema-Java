package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EType;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class MethodNotFoundException extends MultilevelRuntimeException {
    private EType type;

    public MethodNotFoundException(ErrorDetail detail) {
        super(detail);
    }

    public MethodNotFoundException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || type == null) return this;
        return new MethodNotFoundException(formatForSchema(getCode(),
            getMessage() + " in " + type, token), this);
    }
}