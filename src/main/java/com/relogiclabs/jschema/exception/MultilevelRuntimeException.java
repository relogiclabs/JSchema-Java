package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.internal.util.ExceptionLevel;
import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.internal.util.ExceptionLevel.HIGH_LEVEL;
import static com.relogiclabs.jschema.internal.util.ExceptionLevel.LOW_LEVEL;

@Getter
public abstract class MultilevelRuntimeException extends BaseRuntimeException {
    private final ExceptionLevel level;

    public MultilevelRuntimeException(String message) {
        this(null, message, null);
    }

    public MultilevelRuntimeException(String code, String message) {
        this(code, message, null);
    }

    public MultilevelRuntimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
        this.level = LOW_LEVEL;
    }

    public MultilevelRuntimeException(ErrorDetail detail) {
        this(detail, null);
    }

    public MultilevelRuntimeException(ErrorDetail detail, Throwable cause) {
        super(detail, cause);
        this.level = HIGH_LEVEL;
    }

    public abstract RuntimeException translate(Token token);

    public boolean isLowLevel() {
        return level == LOW_LEVEL;
    }

    public boolean isHighLevel() {
        return level == HIGH_LEVEL;
    }
}