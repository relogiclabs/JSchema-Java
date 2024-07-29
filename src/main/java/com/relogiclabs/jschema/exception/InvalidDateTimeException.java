package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.internal.time.DateTimeContext;
import lombok.Getter;

@Getter
public class InvalidDateTimeException extends BaseRuntimeException {
    private final DateTimeContext context;

    public InvalidDateTimeException(String code, String message, DateTimeContext context) {
        this(code, message, context, null);
    }

    public InvalidDateTimeException(String code, String message, DateTimeContext context,
                Throwable cause) {
        super(code, message, cause);
        this.context = context;
    }
}