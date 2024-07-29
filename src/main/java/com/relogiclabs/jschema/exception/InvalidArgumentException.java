package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class InvalidArgumentException extends InvocationRuntimeException {
    private String parameter;

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InvalidArgumentException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    public void setContext(String code, String parameter) {
        setCode(code);
        this.parameter = parameter;
    }

    public abstract InvalidArgumentException failWithFunctionException();
    public abstract InvalidArgumentException failWithMethodException(EValue self);

    protected String formatMethodMessage(EValue self) {
        return getMessage() + " for parameter '" + parameter
            + "' in method '" + getSubject() + "' of " + self.getType();
    }

    protected String formatFunctionMessage() {
        return getMessage() + " for parameter '" + parameter
            + "' of function '" + getSubject() + "'";
    }
}