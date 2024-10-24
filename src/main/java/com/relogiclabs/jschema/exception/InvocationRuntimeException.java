package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.Token;

import java.lang.reflect.Method;

import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getSignature;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
public class InvocationRuntimeException extends MultilevelRuntimeException {
    private String subject;
    private Method method;

    public InvocationRuntimeException(String code, String message) {
        super(code, message);
    }

    public InvocationRuntimeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public RuntimeException translate(Token token) {
        if(isHighLevel() || subject == null) return this;
        return new InvocationRuntimeException(formatForSchema(getCode(),
            addNative(getMessage() + " '" + subject + "'"), token), this);
    }

    protected String addNative(String message) {
        if(method == null) return message;
        return message + " with native: " + getSignature(method);
    }

    protected static String addSelf(String message, EValue self) {
        if(self == null) return message;
        return message + " in " + self.getType();
    }
}