package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;
import org.antlr.v4.runtime.Token;

import static com.relogiclabs.jschema.exception.MultilevelRuntimeException.Level.HIGH_LEVEL;
import static com.relogiclabs.jschema.exception.MultilevelRuntimeException.Level.LOW_LEVEL;

@Getter
public abstract class MultilevelRuntimeException extends BaseRuntimeException {
    public enum Level { LOW_LEVEL, HIGH_LEVEL }
    private final Level level;

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