package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class InvalidContextException extends InvocationRuntimeException {
    private Token mainToken;

    public InvalidContextException(String code, String message, Token mainToken) {
        super(code, message);
        this.mainToken = mainToken;
    }

    public InvalidContextException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public MultilevelRuntimeException translate(Token token) {
        if(isHighLevel() || getSubject() == null) return this;
        return new InvalidContextException(formatForSchema(getCode(),
            getMessage() + " for function '" + getSubject() + "'",
            nonNullFrom(mainToken, token)), this);
    }
}