package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;

public class ArgumentValueException extends InvalidArgumentException {
    public ArgumentValueException(String message) {
        super(message);
    }

    public ArgumentValueException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ArgumentValueException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public InvalidArgumentException failWithFunctionException() {
        return new FunctionArgumentValueException(this);
    }

    @Override
    public InvalidArgumentException failWithMethodException(EValue self) {
        return new MethodArgumentValueException(self, this);
    }
}