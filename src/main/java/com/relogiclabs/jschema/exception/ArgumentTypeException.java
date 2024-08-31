package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EValue;

public class ArgumentTypeException extends InvalidArgumentException {
    public ArgumentTypeException(String message) {
        super(message);
    }

    public ArgumentTypeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ArgumentTypeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
    }

    @Override
    public FunctionArgumentTypeException failWithFunctionException() {
        return new FunctionArgumentTypeException(this);
    }

    @Override
    public MethodArgumentTypeException failWithMethodException(EValue self) {
        return new MethodArgumentTypeException(self, this);
    }
}