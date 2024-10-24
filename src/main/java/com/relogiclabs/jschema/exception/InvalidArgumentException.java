package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;

public class InvalidArgumentException extends InvocationRuntimeException {
    public InvalidArgumentException(String code, String message) {
        super(code, message);
    }

    public InvalidArgumentException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    protected String formatMethodMessage(EValue self) {
        return addNative(getMessage() + " of method '" + getSubject()
            + "' in " + self.getType());
    }

    protected String formatFunctionMessage() {
        return addNative(getMessage() + " of function '" + getSubject() + "'");
    }
}