package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;
import lombok.Setter;

import static java.util.Arrays.copyOfRange;

@Getter @Setter
public class BaseRuntimeException extends RuntimeException {
    // Exception creation method
    private static final String FAIL_METHOD_PREFIX = "fail";
    private String code;

    public BaseRuntimeException(String message) {
        this(null, message, null);
    }

    public BaseRuntimeException(String code, String message) {
        this(code, message, null);
    }

    public BaseRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseRuntimeException(ErrorDetail detail) {
        this(detail, null);
    }

    public BaseRuntimeException(ErrorDetail detail, Throwable cause) {
        super(detail.getMessage(), cause);
        this.code = detail.getCode();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        var result = super.fillInStackTrace();
        formatStackTrace();
        return result;
    }

    private void formatStackTrace() {
        StackTraceElement[] stackTrace = getStackTrace();
        int offset = 0;
        for(var e : stackTrace) {
            if(e.getMethodName().startsWith(FAIL_METHOD_PREFIX)) offset++;
            else break;
        }
        setStackTrace(copyOfRange(stackTrace, offset, stackTrace.length));
    }
}